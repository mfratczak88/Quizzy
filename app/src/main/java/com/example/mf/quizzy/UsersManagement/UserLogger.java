package com.example.mf.quizzy.UsersManagement;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.mf.quizzy.Config.AppConfig;
import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.MainController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class UserLogger {
    String mEmail;
    String mPassword;
    AuthenticationListener mAuthenticationListener;
    ResponseParser mResponseParser = new ResponseParser();

    public UserLogger(String email, String password, AuthenticationListener listener) {
        this.mEmail = email;
        this.mPassword = password;
        this.mAuthenticationListener = listener;
    }

    public String getEmail(){
        return mEmail;
    }

    public String getPassword(){
        return mPassword;
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
                Map<String, String> mapResponse = mResponseParser.stringToMap(response);
                mAuthenticationListener.onSuccess(mapResponse);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("email", loginRequestBody.getString("email"));
                    params.put("password", loginRequestBody.getString("password"));
                } catch (JSONException e) {
                    return null;
                }

                return params; //return the parameters
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

        };
        return stringRequest;
    }

    private class ResponseParser {
        private static final String PARSING_ERROR = "Could not interpret server response";

        private Map<String, String> stringToMap(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                return jsonToMap(jsonResponse);
            } catch (Exception e) {
                return null;
            }
        }


        private Map<String, String> jsonToMap(@NonNull JSONObject jsonObject) {
            Map<String, String> map = new HashMap<>();
            try {
                Iterator<String> keysIterator = jsonObject.keys();
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next();
                    String value = (String) jsonObject.get(key);
                    map.put(key, value);
                }
                return map;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
