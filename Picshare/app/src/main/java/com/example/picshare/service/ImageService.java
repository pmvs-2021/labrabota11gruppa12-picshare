package com.example.picshare.service;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.example.picshare.Metadata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

public class ImageService {
    public static Future<NetworkResponse> addImage(byte[] image) {
        String url = String.format(Locale.US, "https://%s/draw?id=%d", Metadata.serverURL, Metadata.thisUser.getId());
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        FileUploadRequest request = new FileUploadRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                        System.out.println("Id: " + obj.getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("GotError",  error.toString())) {
            @Override
            public Map<String, FileDataPart> getByteData() {
                Map<String, FileDataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new FileDataPart(imagename + ".png", image));
                return params;
            }
        };

        //adding the request to volley
        System.out.println("Request added");
        Metadata.requests.add(request);
        return future;
    }
}
