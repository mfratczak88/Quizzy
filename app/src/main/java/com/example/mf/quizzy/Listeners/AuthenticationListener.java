package com.example.mf.quizzy.Listeners;

import org.json.JSONObject;

public interface AuthenticationListener {
     void onSuccess(String response);
     void onError(String response);
}
