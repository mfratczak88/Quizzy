package com.example.mf.quizzy.Util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.mf.quizzy.App;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.volley.*;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {

    public interface ResponseListener {
        void onResponse(JSONObject response);

        void onError(String cause);
    }

    private static RequestQueue sRequestQueue = App.getInstance().getRequestQueue();


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void httpGetRequest(String url, final ResponseListener listener) {
        JsonObjectRequest getRequestObject = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error Response", error.getMessage());
                        listener.onError(error.getMessage());
                    }
                });
        sRequestQueue.add(getRequestObject);
    }

    public static void httpPostRequest(String url, final JSONObject body, final Response.Listener<String> responseListener, final Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListener.onErrorResponse(error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return body == null ? null : body.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }

            @Override
            protected Map<String, String> getParams() {
                return HttpUtil.json2Map(body);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

        };
        sRequestQueue.add(stringRequest);
    }

    public static Map<String, String> string2Map(String response) {
        return new ResponseParser().stringToMap(response);
    }

    public static Map<String, String> json2Map(@NonNull JSONObject jsonObject) {
        return new ResponseParser().jsonToMap(jsonObject);
    }
}

class ResponseParser {
    private static final String PARSING_ERROR = "Could not interpret server response";

    public Map<String, String> stringToMap(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            return jsonToMap(jsonResponse);
        } catch (Exception e) {
            return null;
        }
    }


    public Map<String, String> jsonToMap(@NonNull JSONObject jsonObject) {
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
