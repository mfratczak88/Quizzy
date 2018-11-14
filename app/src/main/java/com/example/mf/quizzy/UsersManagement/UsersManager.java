package com.example.mf.quizzy.UsersManagement;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.RoomPersistence.User;
import com.example.mf.quizzy.RoomPersistence.UserRepository;
import com.example.mf.quizzy.Sessions.SessionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class UsersManager implements AuthenticationListener {
    private AuthenticationListener mAuthenticationListener;
    private UserRepository mUserRepository;
    private User mCurrentUser;
    private SessionManager mSessionManager;
    private static UsersManager sInstance;
    private UserLogger mUserLogger;
    private UserRegisterer mUserRegisterer;

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
        mSessionManager = new SessionManager(context);
    }

    public void loginUser(String email, String password) throws Exception {
        loadFromLocalDb(email, password);
        if (mCurrentUser != null) {
            mAuthenticationListener.onSuccess(null);
            return;
        }

        mUserLogger = new UserLogger(email, password, this);
        mUserLogger.login();
    }

    public void registerUser(String name, String email, String password) {
    }

    private void loadFromLocalDb(String email, String password) {
        User user = mUserRepository.getUserByEmail(email);
        if (user == null)
            return;

        if (isPasswordGivenAMatch(password, user.getPassword())) {
            mCurrentUser = user;
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
        ResponseToUserMapper mapper = new ResponseToUserMapper(new User(), response);
        User user = mapper.mapResponseToUser();
        user.setPassword(mUserLogger.getPassword());
        return user;
    }

    @Override
    public void onSuccess(Map<String, String> response) {
        mCurrentUser = mapServerResponseToUserObject(response);
        if (mCurrentUser != null) {
            mSessionManager.createLoginSession(mCurrentUser.getName(), mCurrentUser.getEmail());
            addUserToDb(mCurrentUser);
        }
        mAuthenticationListener.onSuccess(response);
    }

    @Override
    public void onError(String response) {
        mAuthenticationListener.onError(response);
    }

    private void addUserToDb(final User user) {
        Activity activity = (Activity) mAuthenticationListener;

        final UserRepository userRepository = new UserRepository(activity.getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.insertUser(user);
            }

        }).start();

    }

    private class ResponseToUserMapper {
        private User mUser;
        private Method[] mUserMethods;
        private Field[] mUserFields;
        private Map<String, String> mReponse;

        private ResponseToUserMapper(User user, Map<String, String> response) {
            mUser = user;
            mReponse = response;
            mUserFields = getUserFields();
            mUserMethods = getUserMethods();
        }

        private User mapResponseToUser() {
            for (Field userField : mUserFields) {
                String fieldName = userField.getName();
                String responseValue = mReponse.get(fieldName);

                if (responseValue != null) {
                    tryToSetUserField(responseValue, fieldName);
                }
            }
            return mUser;
        }

        private Field[] getUserFields() {
            return mUser.getClass().getDeclaredFields();
        }

        private Method[] getUserMethods() {
            return mUser.getClass().getDeclaredMethods();
        }

        private void tryToSetUserField(String responseValue, String fieldName) {
            for (Method userMethod : mUserMethods) {
                String userMethodName = userMethod.getName();
                String probableSetterMethodName = "set" + fieldName;

                try {
                    if (userMethodName.equalsIgnoreCase(probableSetterMethodName)) {
                        userMethod.invoke(mUser, responseValue);
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
