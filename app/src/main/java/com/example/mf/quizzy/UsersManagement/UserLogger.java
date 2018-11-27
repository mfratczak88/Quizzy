package com.example.mf.quizzy.usersManagement;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.roomPersistence.User;
import com.example.mf.quizzy.util.HttpUtil;


import org.json.JSONObject;

import java.util.Map;

class UserLogger  implements BackendConnector{

    private User mUser;
    private Listener mListener;

     UserLogger(User user, Listener listener) {
        this.mUser = user;
        this.mListener = listener;
    }

    @Override
    public void connect() {
        login();
    }

    @Override
    public User getUser() {
        return mUser;
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
            requestBody.put("email", mUser.getEmail());
            requestBody.put("password", mUser.getPassword());
            return requestBody;

        } catch (Exception e) {
            return null;
        }
    }
}
