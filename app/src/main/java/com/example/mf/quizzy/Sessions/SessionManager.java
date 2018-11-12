package com.example.mf.quizzy.Sessions;

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
