package com.example.mf.quizzy.usersManagement;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.listeners.AuthenticationListener;
import com.example.mf.quizzy.roomPersistence.Category;
import com.example.mf.quizzy.roomPersistence.Points;
import com.example.mf.quizzy.roomPersistence.Settings;
import com.example.mf.quizzy.roomPersistence.User;
import com.example.mf.quizzy.roomPersistence.UserRepository;
import com.example.mf.quizzy.sessions.SessionManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersManager {
    private static final String KEY_SAVE_PROGRESS = "save_progress";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_QUESTIONS_PER_SESSION = "questions_per_session";
    private static final String KEY_ANSWER_TIME_IN_SECONDS = "answer_time_in_seconds";

    private static UsersManager sInstance;

    private AuthenticationListener mAuthenticationListener;
    private UserRepository mUserRepository;
    private User mCurrentUser;
    private SessionManager mSessionManager;
    private BackendConnector mBackendConnector;

    public static UsersManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UsersManager(context);
            return sInstance;
        }
        return sInstance;
    }

    private UsersManager(Context context) {
        mUserRepository = new UserRepository(context);
        mSessionManager = new SessionManager(context);
    }

    public void loginUser(LoginCredentials loginCredentials, AuthenticationListener listener) throws Exception {
        setAuthenticationListener(listener);
        if (couldLoadLocally(loginCredentials)) {
            return;
        }

        mBackendConnector = BackendConnectorFactory.getLoginConnector(getNewUserObjectForLogin(loginCredentials), new BackendConnector.Listener() {
            @Override
            public void onSuccess(Map<String, String> response) {
                mCurrentUser = mapServerResponseToUserObject(response);
                if (mCurrentUser != null) {
                    updateSessionManagerOnLogin(mapDefaultConfigSettingsToSettingsEntity());
                    addUserToDb();
                }
                mAuthenticationListener.onSuccess(response);
            }

            @Override
            public void onError(String reason) {
                mAuthenticationListener.onError(reason);
            }
        });
        mBackendConnector.connect();
    }

    public void logOutUser() {
        mSessionManager.logOutUser();
    }


    public void registerUser(RegistrationCredentials registrationCredentials, AuthenticationListener listener) throws Exception {
        setAuthenticationListener(listener);
        mBackendConnector = BackendConnectorFactory.getRegistrationConnector(getNewUserObjectForRegistration(registrationCredentials), new BackendConnector.Listener() {
            @Override
            public void onSuccess(Map<String, String> response) {
                mCurrentUser = mBackendConnector.getUser();
                addUserToDb();
                mAuthenticationListener.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                mAuthenticationListener.onError(error);
            }
        });
        mBackendConnector.connect();
    }

    // todo : this method is way too long, cut it down.
    public void addUserPoints(String categoryName, int amountOfPoints) throws Exception {
        Category category = mUserRepository.getCategoryByName(categoryName);
        if (category == null) {
            throw new Exception("No category of name " + categoryName);
        }
        Points points = mUserRepository.getPointsForUserIdInCategory(mCurrentUser.getId(), category.getId());

        if (points == null) {
            points = new Points();
            points.setCategoryId(category.getId());
            points.setUserId(mCurrentUser.getId());
            points.setTotalPoints(amountOfPoints);
            mUserRepository.insertPoints(points);

        } else {
            points.setTotalPoints(points.getTotalPoints() + amountOfPoints);
            mUserRepository.updatePoints(points);
        }


    }

    public boolean isUserLoggedIn() {
        if (mSessionManager.isLoggedIn()) {
            loadUserFromSessionManager();
            return true;
        }
        return false;
    }

    public Map<String, String> getUserCategoryAndPointsMap() {
        Map<String, String> userPointsMap = new HashMap<>();
        List<Category> categories = mUserRepository.getAllCategories();

        for (Category category : categories) {
            Points pointsInSingleCategory = mUserRepository.getPointsForUserIdInCategory(mCurrentUser.getId(), category.getId());

            if (pointsInSingleCategory != null) {
                userPointsMap.put(category.getName(), String.valueOf(pointsInSingleCategory.getTotalPoints()));
            } else {
                userPointsMap.put(category.getName(), "0");
            }
        }
        return userPointsMap;
    }

    public void setUserSettings(Settings userSettings) {
        mUserRepository.updateSettings(userSettings);
    }

    public Settings getUserSettings() throws Exception {
        Settings settings = getUserSettingsFromSessionManager();
        if (settings == null) {
            throw new Exception("Not found");
        }
        return settings;
    }

    public String getUserName() {
        return mCurrentUser.getName();
    }

    public String getUserEmail() {
        return mCurrentUser.getEmail();
    }

    private void loadUserFromSessionManager() {
        String email = mSessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);
        mCurrentUser = mUserRepository.getUserByEmail(email);
    }


    private boolean couldLoadLocally(LoginCredentials loginCredentials) {
        loadFromLocalDb(loginCredentials);
        if (mCurrentUser != null) {
            updateSessionManagerOnLogin(mUserRepository.getSettingsForUserId(mCurrentUser.getId()));
            mAuthenticationListener.onSuccess(null);
            return true;
        }
        return false;
    }

    private User getNewUserObjectForRegistration(RegistrationCredentials registrationCredentials) {
        User user = new User();
        user.setName(registrationCredentials.getName());
        user.setPassword(registrationCredentials.getPassword());
        user.setEmail(registrationCredentials.getEmail());
        return user;
    }


    private User getNewUserObjectForLogin(LoginCredentials loginCredentials) {
        User user = new User();
        user.setEmail(loginCredentials.getEmail());
        user.setPassword(loginCredentials.getPassword());
        return user;
    }

    private void loadFromLocalDb(LoginCredentials loginCredentials) {
        User user = mUserRepository.getUserByEmail(loginCredentials.getEmail());
        if (user == null)
            return;

        if (isPasswordGivenAMatch(loginCredentials.getPassword(), user.getPassword())) {
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
        ResponseToUserMapper mapper = new ResponseToUserMapper(mBackendConnector.getUser(), response);
        return mapper.mapResponseToUser();
    }


    private void addUserToDb() {
        final Settings settings = mapDefaultConfigSettingsToSettingsEntity();
        final UserRepository userRepository = new UserRepository(App.getInstance().getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.insertUserAndSettings(mCurrentUser, settings);
            }

        }).start();


    }

    private void updateSessionManagerOnLogin(@NonNull Settings settings) {
        mSessionManager.setBooleanValue(KEY_SAVE_PROGRESS, settings.isSaveProgress());
        mSessionManager.setIntValue(KEY_ANSWER_TIME_IN_SECONDS, settings.getAnswerTimeInSeconds());
        mSessionManager.setIntValue(KEY_QUESTIONS_PER_SESSION, settings.getQuestionsPerSession());
        mSessionManager.setStringValue(KEY_LEVEL, settings.getLevel());
        mSessionManager.createLoginSession(mCurrentUser.getName(), mCurrentUser.getEmail());
    }

    private @Nullable
    Settings getUserSettingsFromSessionManager() {
        Settings userSettings = new Settings();
        try {
            userSettings.setLevel(mSessionManager.getStringValue(KEY_LEVEL));
        } catch (Exception e) {
            return null;
        }
        userSettings.setSaveProgress(mSessionManager.getBooleanValue(KEY_SAVE_PROGRESS));
        userSettings.setAnswerTimeInSeconds(mSessionManager.getIntValue(KEY_ANSWER_TIME_IN_SECONDS));
        userSettings.setQuestionsPerSession(mSessionManager.getIntValue(KEY_QUESTIONS_PER_SESSION));
        return userSettings;

    }

    private Settings mapDefaultConfigSettingsToSettingsEntity() {
        AppConfig.UserSettings userDefaultSettings = App.getInstance().getAppConfig().getUserDefaultSettings();
        Settings settings = new Settings();

        settings.setAnswerTimeInSeconds(userDefaultSettings.getAnswerTimeInSeconds());
        settings.setQuestionsPerSession(userDefaultSettings.getQuestionsPerSession());
        settings.setLevel(userDefaultSettings.getLevel());
        settings.setSaveProgress(userDefaultSettings.doSaveProgress());

        return settings;
    }

    private class ResponseToUserMapper {
        private User mUser;
        private Method[] mUserMethods;
        private Field[] mUserFields;
        private Map<String, String> mResponse;

        private ResponseToUserMapper(User user, Map<String, String> response) {
            mUser = user;
            mResponse = response;
            mUserFields = getUserFields();
            mUserMethods = getUserMethods();
        }

        private User mapResponseToUser() {
            for (Field userField : mUserFields) {
                String fieldName = userField.getName();
                String responseValue = mResponse.get(fieldName);

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
                    Log.d(getClass().toString(), "Setting field " + fieldName + " failed");
                }
            }
        }
    }
}
