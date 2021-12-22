package com.example.picshare.service;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.example.picshare.Metadata;
import com.example.picshare.domain.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UserService {

    public static Future<User> findByUserName(String username) {
        String url = String.format("https://%s/user?username=%s", Metadata.INSTANCE.getServerURL(), username);
        RequestFuture<User> future = RequestFuture.newFuture();
        UserRequest request = new UserRequest(Request.Method.GET, url, future, future);
        Metadata.requests.add(request);
        return future;
    }
}
