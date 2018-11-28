package com.example.mf.quizzy.sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

    public SessionManager(Context context) {
        this.mContext = context;
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void createLoginSession(String name, String email) {
        setLoginData(name, email);
    }

    private void setLoginData(String name, String email) {
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

    public void setIntValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void setStringValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void setBooleanValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);

    }

    public String getStringValue(String key) throws Exception {
        String value = mSharedPreferences.getString(key, null);
        if (value == null) {
            throw new Exception("Not found");
        }
        return value;
    }

    public int getIntValue(String key) {
        return mSharedPreferences.getInt(key, 0);
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
