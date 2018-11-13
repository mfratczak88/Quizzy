package com.example.mf.quizzy.Listeners;

import org.json.JSONObject;

import java.util.Map;

public interface AuthenticationListener {
     void onSuccess(Map<String, String> response);
     void onError(String response);
}
