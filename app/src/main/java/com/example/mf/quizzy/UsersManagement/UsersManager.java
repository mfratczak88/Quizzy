package com.example.mf.quizzy.UsersManagement;

import android.app.Activity;
import android.content.Context;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.RoomPersistence.User;
import com.example.mf.quizzy.RoomPersistence.UserRepository;
import com.example.mf.quizzy.Sessions.SessionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class UsersManager implements AuthenticationListener {
    private AuthenticationListener mAuthenticationListener;
    private UserRepository mUserRepository;
    private User mCurrentUser;
    private SessionManager mSessionManager;
    private static UsersManager sInstance;

    public static UsersManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UsersManager(context);
            return sInstance;
        }
        sInstance.setAuthenticationListener((AuthenticationListener) context);
        return sInstance;
    }

    private UsersManager(Context context) {
        this.mAuthenticationListener = (AuthenticationListener) context;
        mUserRepository = new UserRepository(context);
        mSessionManager = new SessionManager(context); // todo : pretty risky here - change it
    }

    public void loginUser(String email, String password) throws Exception {
        loadFromLocalDb(email, password);
        if (mCurrentUser != null) {
            mAuthenticationListener.onSuccess(null);
            return;
        }

        UserLogger userLogger = new UserLogger(email, password, this);
        userLogger.login();
    }

    public void registerUser(String name, String email, String password) {
    }

    private void loadFromLocalDb(String email, String password) {
        try {
            User user = mUserRepository.getUserByEmail(email).getValue();
            if (user == null)
                return;

            if (isPasswordGivenAMatch(password, user.getPassword())) {
                mCurrentUser = user;
            }
        } catch (NullPointerException e) {
            // nothing to do if null
        }
    }

    private boolean isPasswordGivenAMatch(String passedPassword, String dbPassword) {
        // todo add hashing
        return passedPassword.equals(dbPassword);
    }

    private void setAuthenticationListener(AuthenticationListener listener) {
        this.mAuthenticationListener = listener;
    }

    private User mapServerResponseToUserObject(Map<String, String> response) {
        // todo: potential performance issues here, to be checked !

        User user = new User();
        Field[] userFields = user.getClass().getDeclaredFields();
        Method[] userMethods = user.getClass().getDeclaredMethods();

        for (Field userField : userFields) {
            String fieldName = userField.getName();
            String responseValue = response.get(fieldName);

            if (responseValue != null) {
                try {
                    for (Method userMethod : userMethods) {

                        String userMethodName = userMethod.getName();
                        String probableSetterMethod = "set" + fieldName;

                        if (userMethodName.equalsIgnoreCase(probableSetterMethod)) {
                            userMethod.invoke(user, responseValue);
                        }
                    }
                } catch (Exception e) {

                }
            }

        }
        return user;
    }

    @Override
    public void onSuccess(Map<String, String> response) {
        mCurrentUser = mapServerResponseToUserObject(response);
        if(mCurrentUser != null) {
            mSessionManager.createLoginSession(mCurrentUser.getName(), mCurrentUser.getEmail());
            addUserToDb(mCurrentUser);
        }
        mAuthenticationListener.onSuccess(response);
    }

    @Override
    public void onError(String response) {
        mAuthenticationListener.onError(response);
    }

    private void addUserToDb(final User user){
        Activity activity = (Activity) mAuthenticationListener;
        final UserRepository userRepository = new UserRepository(activity.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.insertUser(user);
            }
        }).start();
    }
}
