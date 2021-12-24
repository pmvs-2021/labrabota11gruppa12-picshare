package com.example.picshare.service;

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
    public static Future<JSONObject> addImage(byte[] image) {
        String url = String.format("https://%s/draw", Metadata.INSTANCE.getServerURL());
        Map<String, Object> params = new HashMap<>();
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        params.put("author_id", Metadata.INSTANCE.getThisUser().getId());
        params.put("image", image);
        System.out.println(new JSONObject(params).toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                future, future);
        System.out.println("Request added");
        Metadata.requests.add(request);
        return future;
    }
}
