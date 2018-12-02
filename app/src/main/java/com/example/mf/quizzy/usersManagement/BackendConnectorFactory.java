package com.example.mf.quizzy.usersManagement;


abstract class BackendConnectorFactory {


    static BackendConnector getLoginConnector(LoginCredentials loginCredentials, BackendConnector.Listener listener) {
        return new UserLogger(loginCredentials, listener);
    }

    static BackendConnector getRegistrationConnector(RegistrationCredentials registrationCredentials, BackendConnector.Listener listener) {
        return new UserRegisterer(registrationCredentials, listener);
    }

}
