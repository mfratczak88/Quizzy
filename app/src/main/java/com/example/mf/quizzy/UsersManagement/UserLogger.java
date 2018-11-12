package com.example.mf.quizzy.UsersManagement;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mf.quizzy.Config.AppConfig;
import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.MainController.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

class UserLogger {
    String mEmail;
    String mPassword;
    AuthenticationListener mAuthenticationListener;

    public UserLogger(String email, String password, AuthenticationListener listener) {
        this.mEmail = email;
        this.mPassword = password;
    }

    protected void login() {
        StringRequest stringRequest = getLoginStringRequest(getLoginRequestBody());
        assert stringRequest != null;
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private JSONObject getLoginRequestBody() {
        try {
            JSONObject loginRequestBody = new JSONObject();
            loginRequestBody.put("email", mEmail);
            loginRequestBody.put("password", mPassword);
            return loginRequestBody;
        } catch (JSONException e) {
            // todo: add error handling here
            return null;
        }
    }

    private StringRequest getLoginStringRequest(final JSONObject loginRequestBody) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mAuthenticationListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthenticationListener.onError(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return loginRequestBody == null ? null : loginRequestBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    // todo: add error handling here
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

        };
        return stringRequest;
    }
}
