package com.example.mf.quizzy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.activities.login.LoginActivity;
import com.example.mf.quizzy.activities.mainScreen.MainActivity;
import com.example.mf.quizzy.activities.gameplay.GamePlayActivity;
import com.example.mf.quizzy.activities.register.RegisterActivity;
import com.example.mf.quizzy.activities.results.ResultsActivity;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.roomPersistence.Category;
import com.example.mf.quizzy.roomPersistence.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class App extends Application {
    private static RequestQueue sRequestQueue;
    private static App sInstance;
    private Map<String, String> mCategories = new HashMap<>();
    private AppConfig mAppConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mAppConfig = new AppConfig(getApplicationContext());
    }

    public AppConfig getAppConfig(){
        return mAppConfig;
    }

    public void addToRequestQueue(Request request) {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        sRequestQueue.add(request);
    }

    public RequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    public static synchronized App getInstance() {
        if (sInstance == null) {
            sInstance = new App();
        }
        return sInstance;
    }

    public Intent getLoginIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public Intent getRegisterIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    public Intent getMainIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public Intent getGamePlayIntent(Context context) {
        return new Intent(context, GamePlayActivity.class);
    }

    public Intent getResultIntent(Context context) {
        return new Intent(context, ResultsActivity.class);
    }


}
