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
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void addToRequestQueue(Request request){
        if(sRequestQueue == null){
          sRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        sRequestQueue.add(request);
    }


    // no real way to make it a singleton here...
    public static synchronized AppController getInstance() {
        if (sInstance == null) {
            sInstance = new AppController();
        }
        return sInstance;
    }

}
