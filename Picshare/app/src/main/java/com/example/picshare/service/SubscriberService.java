package com.example.picshare.service;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.picshare.Metadata;
import com.example.picshare.domain.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class SubscriberService {
    public static Future<User[]> getSubscribers(String id) {
        String url = String.format("https://%s/subscriber?id=%s", Metadata.serverURL, id);
        RequestFuture<User[]> future = RequestFuture.newFuture();
        UserArrayRequest request = new UserArrayRequest(Request.Method.GET, url, future, future);
        Metadata.requests.add(request);
        return future;
    }

    public static void subscribe(int subscriberId, int userId) {
        String url = String.format("https://%s/subscriber", Metadata.serverURL);
        Map<String, Integer> params = new HashMap<>();
        params.put("user_id",userId);
        params.put("subscriber_id", subscriberId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                System.out::println, System.out::println);

        Metadata.requests.add(request);
    }

}
