package com.example.mf.quizzy.config;

import android.content.Context;
import android.util.Log;


import com.example.mf.quizzy.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppConfig {
    private Context mContext;
    private String mLoginUrl, mRegisterUrl, mApiBaseUrl;
    private Map<String, String> mCategories = new HashMap<>();
    private UserSettings mUserDefaultSettings;


    public AppConfig(Context context) {
        mContext = context;
        loadCategories();
        loadUrls();
        loadUserDefaultSettings();
    }

    public UserSettings getUserDefaultSettings() {
        return mUserDefaultSettings;
    }

    public String getLoginUrl() {
        return mLoginUrl;
    }

    public String getRegisterUrl() {
        return mRegisterUrl;
    }

    public String getApiBaseUrl() {
        return mApiBaseUrl;
    }

    public Map<String, String> getCategories() {
        return mCategories;
    }

    public String getUrlForCategory(String categoryName) {
        String categoryNumber = mCategories.get(categoryName);
        return getApiBaseUrl() + categoryNumber;
    }

    private void loadUrls() {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.urls);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            Log.d("App config", "Could not load URL's");
            return;
        }

        for (Object url : properties.keySet()) {
            if (url.toString().contains("login")) {
                mLoginUrl = properties.getProperty((String) url);
            } else if (url.toString().contains("register")) {
                mRegisterUrl = properties.getProperty((String) url);
            } else if (url.toString().contains("api")) {
                mApiBaseUrl = properties.getProperty((String) url);
            }
        }
    }

    private void loadCategories() {
        Properties properties = new Properties();
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.categories);
        try {
            properties.load(inputStream);
            for (Object category : properties.keySet()) {
                mCategories.put(category.toString(), properties.getProperty((String) category));
            }
        } catch (Exception e) {
            Log.d("App config", "Could not load categories");
        }
    }

    private void loadUserDefaultSettings() {
        mUserDefaultSettings = new UserSettings();
        Properties properties = new Properties();
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.user_settings);
        try {
            properties.load(inputStream);
            for (Object userSetting : properties.keySet()) {
                switch ((userSetting.toString())) {
                    case "save_progress":
                        mUserDefaultSettings.setSaveProgress(Boolean.valueOf((String) properties.get(userSetting)));
                        break;
                    case "level":
                        mUserDefaultSettings.setLevel((String) properties.get(userSetting));
                        break;
                    case "questions_per_session":
                        mUserDefaultSettings.setQuestionsPerSession(Integer.parseInt((String) properties.get(userSetting)));
                        break;
                    case "answer_time_in_seconds":
                        mUserDefaultSettings.setAnswerTimeInSeconds(Integer.parseInt((String) properties.get(userSetting)));
                        break;
                }
            }
        } catch (Exception e) {
            Log.d("App config", "Could not load user settings");
        }
    }

    public static class UserSettings {
        public static final String LEVEL_EASY = "Easy";
        public static final String LEVEL_MEDIUM="Medium";
        public static final String LEVEL_HARD="Hard";

        private boolean mSaveProgress;
        private String mLevel;
        private int mQuestionsPerSession;
        private int mAnswerTimeInSeconds;

        public UserSettings(boolean saveProgress, String level, int questionsPerSession, int answerTimeInSeconds) {
            mSaveProgress = saveProgress;
            mLevel = level;
            mQuestionsPerSession = questionsPerSession;
            mAnswerTimeInSeconds = answerTimeInSeconds;
        }

        public UserSettings() {
        }

        public boolean doSaveProgress() {
            return mSaveProgress;
        }

        public void setSaveProgress(boolean saveProgress) {
            mSaveProgress = saveProgress;
        }

        public String getLevel() {
            return mLevel;
        }

        public void setLevel(String level) {
            mLevel = level;
        }

        public int getQuestionsPerSession() {
            return mQuestionsPerSession;
        }

        public void setQuestionsPerSession(int questionsPerSession) {
            mQuestionsPerSession = questionsPerSession;
        }

        public int getAnswerTimeInSeconds() {
            return mAnswerTimeInSeconds;
        }

        public void setAnswerTimeInSeconds(int answerTimeInSeconds) {
            mAnswerTimeInSeconds = answerTimeInSeconds;
        }
    }
}
