package com.example.mf.quizzy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.Activities.Login.LoginActivity;
import com.example.mf.quizzy.Activities.MainScreen.MainActivity;
import com.example.mf.quizzy.Activities.GamePlay.QuestionActivity;
import com.example.mf.quizzy.Activities.Register.RegisterActivity;
import com.example.mf.quizzy.Activities.Results.ResultsActivity;

public class App extends Application {
    private static RequestQueue sRequestQueue;
    private static App sInstance;

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



}
