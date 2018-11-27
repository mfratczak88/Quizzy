package com.example.mf.quizzy.usersManagement;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.roomPersistence.User;
import com.example.mf.quizzy.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;


class UserRegisterer implements BackendConnector {

    private User mUser;
    private Listener mListener;

    UserRegisterer(User user, Listener listener) {
        mUser = user;
        mListener = listener;
    }

    @Override
    public void connect() {
        register();
    }

    @Override
    public User getUser() {
        return this.mUser;
    }

    private JSONObject createRequestBody() throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", mUser.getName());
        requestBody.put("email", mUser.getEmail());
        requestBody.put("password", mUser.getPassword());
        return requestBody;
    }

    private void register() {
        try {
            final JSONObject requestBody = createRequestBody();
            HttpUtil.httpPostRequest(App.getInstance().getAppConfig().getRegisterUrl(), requestBody, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mListener.onSuccess(HttpUtil.string2Map(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mListener.onError(error.getMessage());
                }
            });
        } catch (Exception e) {
            mListener.onError((e.getMessage()));
        }
    }
}