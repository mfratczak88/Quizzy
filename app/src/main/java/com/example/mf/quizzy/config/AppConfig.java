package com.example.mf.quizzy.config;

import android.content.Context;
import android.support.annotation.Nullable;
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
    private UserDefaultSettings mUserDefaultSettings;
    private boolean mLoadCategoriesToDb;


    public AppConfig(Context context) {
        mContext = context;
        loadCategories();
        loadUrls();
        loadUserDefaultSettings();
        loadMigrationProperties();
    }

    private void loadMigrationProperties() {
        try {
            Properties properties = getPropertiesForRawFile(R.raw.migration);
            for (Map.Entry property : properties.entrySet()) {
                if (property.getKey().equals("load_categories_to_db")) {
                    mLoadCategoriesToDb = Boolean.parseBoolean((String) property.getValue());
                }
            }
        } catch (Exception e) {
            Log.d("App Config", "Could not load migration properties correctly");
        }
    }

    public boolean shouldLoadCategoriesToDb() {
        return mLoadCategoriesToDb;
    }


    public UserDefaultSettings getUserDefaultSettings() {
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

    private @Nullable
    Properties getPropertiesForRawFile(int rawResourceId) {
        try {
            InputStream inputStream = mContext.getResources().openRawResource(rawResourceId);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            return null;
        }
    }

    private void loadUrls() {
        try {
            Properties properties = getPropertiesForRawFile(R.raw.urls);
            for (Object url : properties.keySet()) {
                if (url.toString().contains("login")) {
                    mLoginUrl = properties.getProperty((String) url);
                } else if (url.toString().contains("register")) {
                    mRegisterUrl = properties.getProperty((String) url);
                } else if (url.toString().contains("api")) {
                    mApiBaseUrl = properties.getProperty((String) url);
                }
            }
        } catch (Exception e) {
            Log.d("App config", "Could not load URL's");
        }
    }

    private void loadCategories() {
        try {
            Properties properties = getPropertiesForRawFile(R.raw.categories);
            for (Object category : properties.keySet()) {
                mCategories.put(category.toString(), properties.getProperty((String) category));
            }
        } catch (Exception e) {
            Log.d("App config", "Could not load categories");
        }
    }

    private void loadUserDefaultSettings() {
        try {
            mUserDefaultSettings = new UserDefaultSettings();
            Properties properties = getPropertiesForRawFile(R.raw.user_settings);
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

    public static class UserDefaultSettings {
        public static final String LEVEL_EASY = "Easy";
        public static final String LEVEL_MEDIUM = "Medium";
        public static final String LEVEL_HARD = "Hard";

        private boolean mSaveProgress;
        private String mLevel;
        private int mQuestionsPerSession;
        private int mAnswerTimeInSeconds;

        public UserDefaultSettings(boolean saveProgress, String level, int questionsPerSession, int answerTimeInSeconds) {
            mSaveProgress = saveProgress;
            mLevel = level;
            mQuestionsPerSession = questionsPerSession;
            mAnswerTimeInSeconds = answerTimeInSeconds;
        }

        UserDefaultSettings() {
        }

        public boolean doSaveProgress() {
            return mSaveProgress;
        }

        void setSaveProgress(boolean saveProgress) {
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

        void setQuestionsPerSession(int questionsPerSession) {
            mQuestionsPerSession = questionsPerSession;
        }

        public int getAnswerTimeInSeconds() {
            return mAnswerTimeInSeconds;
        }

        void setAnswerTimeInSeconds(int answerTimeInSeconds) {
            mAnswerTimeInSeconds = answerTimeInSeconds;
        }
    }
}
