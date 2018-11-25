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

    public AppConfig(Context context) {
        mContext = context;
        loadCategories();
        loadUrls();
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
            for (Object category : properties.keySet()) {mCategories.put(category.toString(), properties.getProperty((String) category));
            }
        } catch (Exception e) {
            Log.d("App config", "Could not load categories");
        }
    }

}
