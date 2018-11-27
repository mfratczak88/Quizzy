package com.example.mf.quizzy.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.config.AppConfig;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private SharedPreferences mSharedPreferences;
    private Editor mEditor;
    private Context mContext;

    private static final int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "com.example.mf.quizzy";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    private static final String KEY_SAVE_PROGRESS = "save_progress";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_QUESTIONS_PER_SESSION = "questions_per_session";
    private static final String KEY_ANSWER_TIME_IN_SECONDS = "answer_time_in_seconds";

    public SessionManager(Context context) {
        this.mContext = context;
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void createLoginSession(String name, String email) {
        AppConfig.UserSettings userSettings = App.getInstance().getAppConfig().getUserDefaultSettings();
        setLoginData(name, email);
        setUserSettings(userSettings);
    }

    private void setLoginData(String name, String email){
        mEditor
                .putBoolean(IS_LOGGED_IN, true)
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email);
        mEditor.commit();
    }


    public Map<String, String> getUserDetails() {
        Map<String, String> user = new HashMap<>();
        user.put(KEY_NAME, mSharedPreferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, mSharedPreferences.getString(KEY_EMAIL, null));
        return user;
    }

    public AppConfig.UserSettings getUserSettings() {
        return new AppConfig.UserSettings(
                mSharedPreferences.getBoolean(KEY_SAVE_PROGRESS, false),
                mSharedPreferences.getString(KEY_LEVEL, null),
                mSharedPreferences.getInt(KEY_QUESTIONS_PER_SESSION, 0),
                mSharedPreferences.getInt(KEY_ANSWER_TIME_IN_SECONDS, 0));
    }

    public void setUserSettings (AppConfig.UserSettings userSettings){
        mEditor
                .putString(KEY_LEVEL, userSettings.getLevel())
                .putBoolean(KEY_SAVE_PROGRESS, userSettings.doSaveProgress())
                .putInt(KEY_QUESTIONS_PER_SESSION, userSettings.getQuestionsPerSession())
                .putInt(KEY_ANSWER_TIME_IN_SECONDS, userSettings.getAnswerTimeInSeconds());
        mEditor.commit();
    }

    public void logOutUser() {
        clearEditor();
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    private void clearEditor() {
        mEditor.clear();
        mEditor.commit();
    }

}
