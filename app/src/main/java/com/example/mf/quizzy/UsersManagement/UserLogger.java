package com.example.mf.quizzy.UsersManagement;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mf.quizzy.Config.AppConfig;
import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.RoomPersistence.User;
import com.example.mf.quizzy.Util.HttpUtil;


import org.json.JSONObject;

import java.util.Map;

class UserLogger {
    // todo -> extract an abstract class of authenticator with getUser method and abstract createRequestBody
    interface LoginListener{
        void onSuccess(Map<String, String> response);
        void onError(String reason);
    }

    private User mUser;
    private LoginListener mListener;

    protected UserLogger(User user, LoginListener listener) {
        this.mUser = user;
        this.mListener = listener;
    }

    protected User getUser() {
        return mUser;
    }

    protected void login() {
        HttpUtil.httpPostRequest(AppConfig.URL_LOGIN, createRequestBody(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> mapResponse = HttpUtil.string2Map(response);
                mListener.onSuccess(mapResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.onError(error.toString());
            }
        });
    }

    private JSONObject createRequestBody() {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", mUser.getEmail());
            requestBody.put("password", mUser.getPassword());
            return requestBody;

        } catch (Exception e) {
            return null;
        }
    }
}
