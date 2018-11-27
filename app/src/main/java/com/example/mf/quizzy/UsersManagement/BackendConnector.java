package com.example.mf.quizzy.usersManagement;

import com.example.mf.quizzy.roomPersistence.User;

import java.util.Map;

interface BackendConnector {
    interface Listener{
        void onSuccess(Map<String, String> response);
        void onError(String errorCause);
    }
    void connect();
    User getUser();

}
