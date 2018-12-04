package com.example.mf.quizzy.usersManagement;

import com.example.mf.quizzy.listeners.AuthenticationListener;

import java.util.List;

public interface UsersManager {
    void loginUser(LoginCredentials loginCredentials, AuthenticationListener listener) throws Exception;

    void logOutUser();

    void registerUser(RegistrationCredentials registrationCredentials, AuthenticationListener listener) throws Exception;

    boolean isUserLoggedIn();

    UserSettings getUserSettings();

    void saveUserSettings(UserSettings userSettings);

    String getUserName();

    String getUserEmail();

    void saveNewUserResultInCategory(UserResultInCategory newUserResultInCategory) throws Exception;

    List<UserResultInCategory> getUserResultsInCategoryList();

    UserResultInCategory getUserResultInCategory(String categoryName) throws Exception;
}
