package com.example.picshare.service;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.picshare.Metadata;
import com.example.picshare.domain.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class ImageService {
    public static Future<NetworkResponse> addImage(byte[] image) {
        String url = String.format("https://%s/draw", Metadata.INSTANCE.getServerURL());
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        FileUploadRequest request = new FileUploadRequest(Request.Method.POST, url, future, future);
     //   request.getHeaders().put("author_id", String.valueOf(Metadata.INSTANCE.getThisUser().getId()));
        System.out.println("Request added");
        Metadata.requests.add(request);
        return future;
    }
}
