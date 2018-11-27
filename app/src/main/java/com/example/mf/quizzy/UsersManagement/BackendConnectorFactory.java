package com.example.mf.quizzy.usersManagement;

import com.example.mf.quizzy.roomPersistence.User;

abstract class BackendConnectorFactory {


    static BackendConnector getLoginConnector(User user, BackendConnector.Listener listener){
        return new UserLogger(user, listener);
    }

    static BackendConnector getRegistrationConnector(User user, BackendConnector.Listener listener){
        return new UserRegisterer(user, listener);
    }

}
