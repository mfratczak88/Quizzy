package com.example.mf.quizzy.usersManagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.listeners.AuthenticationListener;
import com.example.mf.quizzy.roomPersistence.Category;
import com.example.mf.quizzy.roomPersistence.Points;
import com.example.mf.quizzy.roomPersistence.Settings;
import com.example.mf.quizzy.roomPersistence.User;
import com.example.mf.quizzy.roomPersistence.UserRepository;
import com.example.mf.quizzy.sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class UsersManagerImpl implements UsersManager {
    private static final String KEY_SAVE_PROGRESS = "save_progress";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_QUESTIONS_PER_SESSION = "questions_per_session";
    private static final String KEY_ANSWER_TIME_IN_SECONDS = "answer_time_in_seconds";
    private static UsersManagerImpl sInstance;

    private AuthenticationListener mAuthenticationListener;
    private UserRepository mUserRepository;
    private User mCurrentUser;
    private SessionManager mSessionManager;
    private com.example.mf.quizzy.usersManagement.BackendConnector mBackendConnector;

    static UsersManagerImpl getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UsersManagerImpl(context);
            return sInstance;
        }
        return sInstance;
    }

    private UsersManagerImpl(Context context) {
        mUserRepository = new UserRepository(context);
        mSessionManager = new SessionManager(context);
    }

    @Override
    public void loginUser(final LoginCredentials loginCredentials, AuthenticationListener listener) throws Exception {
        setAuthenticationListener(listener);

        mBackendConnector = BackendConnectorFactory.getLoginConnector(loginCredentials, new BackendConnector.Listener() {
            @Override
            public void onSuccess(Map<String, String> response) {
                mCurrentUser = mapServerResponseToUserObject(response, mapLoginCredentialsToUserObject(loginCredentials));
                if (mCurrentUser == null) {
                    onError("Could not map user response to the user");
                    return;
                }
                updateSessionManagerOnLogin(Settings.mapDefaultConfigSettingsToSettingsEntity());
                addUserToDb();
                mAuthenticationListener.onSuccess(response);
            }

            @Override
            public void onError(String reason) {
                mAuthenticationListener.onError(reason);
            }
        });
        mBackendConnector.connect();
    }

    @Override
    public void logOutUser() {
        mSessionManager.logOutUser();
    }

    @Override
    public void registerUser(RegistrationCredentials registrationCredentials, AuthenticationListener listener) throws Exception {
        setAuthenticationListener(listener);
        mBackendConnector = BackendConnectorFactory.getRegistrationConnector(registrationCredentials, new BackendConnector.Listener() {
            @Override
            public void onSuccess(Map<String, String> response) {
                mAuthenticationListener.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                mAuthenticationListener.onError(error);
            }
        });
        mBackendConnector.connect();
    }

    @Override
    public void saveNewUserResultInCategory(UserResultInCategory newUserResultInCategory) throws Exception {
        Category category = mUserRepository.getCategoryByName(newUserResultInCategory.getCategoryName()); // double check;
        if (category == null) {
            throw new Exception("category not found");
        }
        Points points = getPointsForCategory(category);
        points.setTotalPoints(newUserResultInCategory.getCategoryPoints());

        if (points.getId() == 0) {
            mUserRepository.insertPoints(points);
        } else {
            mUserRepository.updatePoints(points);
        }
    }

    @Override
    public UserResultInCategory getUserResultInCategory(String categoryName) throws Exception {
        Category category = mUserRepository.getCategoryByName(categoryName);
        if (category == null) {
            throw new ClassNotFoundException();
        }
        Points points = getPointsForCategory(category);
        return new UserResultInCategoryImpl(points.getTotalPoints(), category.getName());
    }

    private Points getPointsForCategory(Category category) {
        Points points = mUserRepository.getPointsForUserIdInCategory(mCurrentUser.getId(), category.getId());
        if (points == null) {
            points = new Points();
            points.setCategoryId(category.getId());
            points.setUserId(mCurrentUser.getId());
            mUserRepository.insertPoints(points);
        }
        return points;
    }

    @Override
    public List<UserResultInCategory> getUserResultsInCategoryList() {
        List<UserResultInCategory> userResultInCategoryList = new ArrayList<>();
        for (Category category : mUserRepository.getAllCategories()) {
            Points points = getPointsForCategory(category);
            userResultInCategoryList.add(new UserResultInCategoryImpl(points.getTotalPoints(), category.getName()));
        }
        return userResultInCategoryList;
    }

    @Override
    public boolean isUserLoggedIn() {
        if (mSessionManager.isLoggedIn()) {
            loadUserFromSessionManager();
            return true;
        }
        return false;
    }


    @Override
    public String getUserName() {
        return mCurrentUser.getName();
    }

    @Override
    public String getUserEmail() {
        return mCurrentUser.getEmail();
    }

    @Override
    public void saveUserSettings(UserSettings userSettings) {
       Settings settings = mapUserSettingsToRoomSettings(userSettings);
        updateUserSettingsOnSessionManager(settings);
        mUserRepository.updateSettings(settings);
    }

    @Override
    public UserSettings getUserSettings() {
        Settings settings = getUserSettingsFromSessionManager();
        if (settings == null) { // quite impossible to happen, yet...
            settings = Settings.mapDefaultConfigSettingsToSettingsEntity();
        }
        return UsersManagementFactory.getUserSettings(settings);

    }

    private void loadUserFromSessionManager() {
        String email = mSessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);
        mCurrentUser = mUserRepository.getUserByEmail(email);
    }

    private Settings mapUserSettingsToRoomSettings(UserSettings userSettings){
        Settings settings = mUserRepository.getSettingsForUserId(mCurrentUser.getId());
        settings.setLevel(userSettings.getLevel());
        settings.setAnswerTimeInSeconds(userSettings.getAnswerTimeInSeconds());
        settings.setQuestionsPerSession(userSettings.getQuestionsPerSession());
        settings.setSaveProgress(userSettings.doSaveProgress());
        return settings;
    }

    private User mapLoginCredentialsToUserObject(com.example.mf.quizzy.usersManagement.LoginCredentials loginCredentials) {
        User user = new User();
        user.setEmail(loginCredentials.getEmail());
        return user;
    }

    private void setAuthenticationListener(AuthenticationListener listener) {
        this.mAuthenticationListener = listener;
    }

    private User mapServerResponseToUserObject(Map<String, String> response, User user) {
        ResponseToUserMapper mapper = new ResponseToUserMapper(user, response);
        return mapper.mapResponseToUser();
    }

    private void addUserToDb() {
        final Settings settings = Settings.mapDefaultConfigSettingsToSettingsEntity();
        final UserRepository userRepository = new UserRepository(App.getInstance().getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                userRepository.insertUserAndSettings(mCurrentUser, settings);
            }

        }).start();
    }

    private void updateUserSettingsOnSessionManager(@NonNull Settings settings){
        mSessionManager.setBooleanValue(KEY_SAVE_PROGRESS, settings.isSaveProgress());
        mSessionManager.setIntValue(KEY_ANSWER_TIME_IN_SECONDS, settings.getAnswerTimeInSeconds());
        mSessionManager.setIntValue(KEY_QUESTIONS_PER_SESSION, settings.getQuestionsPerSession());
        mSessionManager.setStringValue(KEY_LEVEL, settings.getLevel());
    }

    private void updateSessionManagerOnLogin(@NonNull Settings settings) {
       updateUserSettingsOnSessionManager(settings);
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
}
