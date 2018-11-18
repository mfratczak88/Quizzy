package com.example.mf.quizzy.UsersManagement;

import com.example.mf.quizzy.RoomPersistence.User;

import java.util.Map;

abstract class BackendConnectorFactory {


    static BackendConnector getLoginConnector(User user, BackendConnector.Listener listener){
        return new UserLogger(user, listener);
    }

    static BackendConnector getRegistrationConnector(User user, BackendConnector.Listener listener){
        return new UserRegisterer(user, listener);
    }

}
