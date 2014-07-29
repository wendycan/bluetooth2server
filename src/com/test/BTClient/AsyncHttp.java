package com.test.BTClient;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.test.BTClient.Config;

public class AsyncHttp {
	public static void makeGetRequest() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://www.baidu.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
            }
        });
    }
	public static void makePostRequest(Context context, float v, String t_iso) throws JSONException, UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + Config.global_write_key);
        client.addHeader("Content-Type", "application/json");
        //RequestParams params = new RequestParams("v", v);
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("v", v);
        jsonParams.put("t", t_iso);
        StringEntity entity = new StringEntity(jsonParams.toString());
        String url = Config.apiPrefix + "/" + "datastreams/temp/datapoints";
        client.post(context, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                System.out.println(response);
            }
        });
    }
}
