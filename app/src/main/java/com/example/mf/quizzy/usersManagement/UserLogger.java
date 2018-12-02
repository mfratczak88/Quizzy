package com.example.mf.quizzy.usersManagement;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.roomPersistence.User;
import com.example.mf.quizzy.util.HttpUtil;


import org.json.JSONObject;

import java.util.Map;

class UserLogger implements BackendConnector {

    private LoginCredentials mLoginCredentials;
    private Listener mListener;

    UserLogger(LoginCredentials loginCredentials, Listener listener) {
        mLoginCredentials = loginCredentials;
        mListener = listener;
    }

    @Override
    public void connect() {
        login();
    }

    private void login() {
        HttpUtil.httpPostRequest(App.getInstance().getAppConfig().getLoginUrl(), createRequestBody(), new Response.Listener<String>() {
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
            requestBody.put("email", mLoginCredentials.getEmail());
            requestBody.put("password", mLoginCredentials.getPassword());
            return requestBody;

        } catch (Exception e) {
            return null;
        }
    }
}
