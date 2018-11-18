package com.example.mf.quizzy.UsersManagement;

import com.example.mf.quizzy.RoomPersistence.User;

import java.util.Map;

interface BackendConnector {
    interface Listener{
        void onSuccess(Map<String, String> response);
        void onError(String errorCause);
    }
    void connect();
    User getUser();

}
