package com.example.mf.quizzy.Model;

import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.MainActivity;
import com.example.mf.quizzy.MainController.AppController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.android.volley.*;

import org.json.JSONObject;

public class HttpUtil {
    interface ResponseListener {
        void onResponse(JSONObject response);

        void onError(String cause);
    }

    private static RequestQueue sRequestQueue = AppController.getInstance().getRequestQueue();


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

}
