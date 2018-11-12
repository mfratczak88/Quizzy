package com.example.mf.quizzy.MainController;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    private static RequestQueue sRequestQueue;
    private static AppController sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sRequestQueue = Volley.newRequestQueue(this);
    }

    public void addToRequestQueue(Request request){
        assert sRequestQueue != null;
        sRequestQueue.add(request);
    }


    // no real way to make it a singleton here...
    public static AppController getInstance() {
        if (sInstance == null) {
            sInstance = new AppController();
        }
        return sInstance;
    }

}
