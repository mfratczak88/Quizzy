package com.example.mf.quizzy.listeners;

import java.util.Map;

public interface AuthenticationListener {
     void onSuccess(Map<String, String> response);
     void onError(String response);
}
