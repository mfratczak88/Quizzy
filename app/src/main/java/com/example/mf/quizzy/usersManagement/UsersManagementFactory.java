package com.example.mf.quizzy.usersManagement;

import android.content.Context;

import com.example.mf.quizzy.roomPersistence.Settings;

public class UsersManagementFactory {
    public static UsersManager getUsersManager(Context context) {
        return UsersManagerImpl.getInstance(context);
    }

    static UserSettings getUserSettings(Settings settings){
        return new UserSettingsImpl(settings);
    }

}
