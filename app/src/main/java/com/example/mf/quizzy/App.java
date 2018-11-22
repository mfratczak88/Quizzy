package com.example.mf.quizzy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.Activities.Login.LoginActivity;
import com.example.mf.quizzy.Activities.MainScreen.MainActivity;
import com.example.mf.quizzy.Activities.GamePlay.QuestionActivity;
import com.example.mf.quizzy.Activities.Register.RegisterActivity;
import com.example.mf.quizzy.Activities.Results.ResultsActivity;
import com.example.mf.quizzy.Config.AppConfig;
import com.example.mf.quizzy.RoomPersistence.Category;
import com.example.mf.quizzy.RoomPersistence.UserRepository;

import java.util.ArrayList;
import java.util.Map;

public class App extends Application {
    private static RequestQueue sRequestQueue;
    private static App sInstance;
    public static final AppConfig APP_CONFIG = new AppConfig();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());

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

    // no real way to make it a singleton here...
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
        return new Intent(context, QuestionActivity.class);
    }

    public Intent getResultIntent(Context context) {
        return new Intent(context, ResultsActivity.class);
    }

    private void initDB() {
        UserRepository userRepository = new UserRepository(this);

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        Category category;

        for (Map.Entry<String, String> entry : APP_CONFIG.CATEGORIES.entrySet()) {
            category = new Category();
            category.setName(entry.getKey());
            category.setExternalId(entry.getValue());
            categoryArrayList.add(category);
        }

        for (Category c : categoryArrayList) {
            try {
                Category dbCategory = userRepository.getCategoryByExternalId(c.getExternalId());
                if (dbCategory == null) {
                    userRepository.insertCategory(c);
                }
            } catch (Exception e) {
                Log.d(getClass().toString(), e.toString());
            }
        }
    }
}
