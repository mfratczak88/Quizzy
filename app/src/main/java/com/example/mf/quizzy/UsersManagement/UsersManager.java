package com.example.mf.quizzy.UsersManagement;

import android.content.Context;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.RoomPersistence.User;
import com.example.mf.quizzy.RoomPersistence.UserRepository;
import com.example.mf.quizzy.Sessions.SessionManager;

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

    private User mapServerResponseToUserObject(String response) {
        return null; // for now
    }

    @Override
    public void onSuccess(String response) {
        mCurrentUser = mapServerResponseToUserObject(response);
        mSessionManager.createLoginSession(mCurrentUser.getName(), mCurrentUser.getEmail());
        mAuthenticationListener.onSuccess(response);
    }

    @Override
    public void onError(String response) {
        mAuthenticationListener.onError(response);
    }
}
