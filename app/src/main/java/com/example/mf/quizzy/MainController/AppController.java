package com.example.mf.quizzy.MainController;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.LoginActivity;
import com.example.mf.quizzy.MainActivity;
import com.example.mf.quizzy.QuestionActivity;
import com.example.mf.quizzy.RegisterActivity;
import com.example.mf.quizzy.ResultsActivity;

public class AppController extends Application {
    private static RequestQueue sRequestQueue;
    private static AppController sInstance;

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
    public static synchronized AppController getInstance() {
        if (sInstance == null) {
            sInstance = new AppController();
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



}
