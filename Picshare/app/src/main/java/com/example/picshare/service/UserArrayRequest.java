package com.example.picshare.service;

import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.picshare.domain.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

public class UserArrayRequest extends Request<User[]> {
    private final Object mLock = new Object();

    @Nullable
    @GuardedBy("mLock")
    private Response.Listener<User[]> mListener;

    public UserArrayRequest(
            int method,
            String url,
            @Nullable Response.Listener<User[]> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<User[]> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode != 200){
            return Response.error(new VolleyError("Not found"));
        }
        String parsedString;
        try {
            parsedString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsedString = new String(response.data);
        }
        User[] parsedUser;
        try {
            parsedUser = new Gson().fromJson(parsedString, User[].class);
        } catch (JsonSyntaxException e) {
            return Response.error(new VolleyError("Wrong JSON format"));
        }
        return Response.success(parsedUser, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(User[] response) {
        Response.Listener<User[]> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }
}
