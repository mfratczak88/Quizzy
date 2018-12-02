package com.example.mf.quizzy.usersManagement;

import android.util.Log;

import com.example.mf.quizzy.roomPersistence.User;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

class ResponseToUserMapper {
    private User mUser;
    private Method[] mUserMethods;
    private Field[] mUserFields;
    private Map<String, String> mResponse;

    ResponseToUserMapper(User user, Map<String, String> response) {
        mUser = user;
        mResponse = response;
        mUserFields = getUserFields();
        mUserMethods = getUserMethods();
    }

    User mapResponseToUser() {
        for (Field userField : mUserFields) {
            String fieldName = userField.getName();
            String responseValue = mResponse.get(fieldName);

            if (responseValue != null) {
                tryToSetUserField(responseValue, fieldName);
            }
        }
        return mUser;
    }

    private Field[] getUserFields() {
        return mUser.getClass().getDeclaredFields();
    }

    private Method[] getUserMethods() {
        return mUser.getClass().getDeclaredMethods();
    }

    private void tryToSetUserField(String responseValue, String fieldName) {
        for (Method userMethod : mUserMethods) {
            String userMethodName = userMethod.getName();
            String probableSetterMethodName = "set" + fieldName;

            try {
                if (userMethodName.equalsIgnoreCase(probableSetterMethodName)) {
                    userMethod.invoke(mUser, responseValue);
                }
            } catch (Exception e) {
                Log.d(getClass().toString(), "Setting field " + fieldName + " failed");
            }
        }
    }
}

