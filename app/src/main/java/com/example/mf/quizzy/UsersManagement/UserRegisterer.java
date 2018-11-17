package com.example.mf.quizzy.UsersManagement;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.mf.quizzy.Config.AppConfig;
import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.RoomPersistence.User;
import com.example.mf.quizzy.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

class UserRegisterer {
    interface RegistrationListener {
        void onSuccess(Map<String, String> response);

        void onError(String error);
    }

    private User mUser;
    private RegistrationListener mListener;

    public UserRegisterer(User user, RegistrationListener listener) {
        mUser = user;
        mListener = listener;
    }

    protected User getUser() {
        return this.mUser;
    }

    private JSONObject createRequestBody() throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", mUser.getName());
        requestBody.put("email", mUser.getEmail());
        requestBody.put("password", mUser.getPassword());
        return requestBody;
    }

    protected void register() {
        try {
            final JSONObject requestBody = createRequestBody();
            HttpUtil.httpPostRequest(AppConfig.URL_REGISTER, requestBody, new Response.Listener<String>() {
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
            return;
        }
    }
}