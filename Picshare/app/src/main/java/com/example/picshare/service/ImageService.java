package com.example.picshare.service;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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
    private static final int TIMEOUT_MS = 60000;

    public static Future<NetworkResponse> addImage(byte[] image) {

        String url = String.format(Locale.US, "https://%s/draw?id=%d", Metadata.serverURL, Metadata.thisUser.getId());
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        FileUploadRequest request = new FileUploadRequest(Request.Method.POST, url, future, future) {
            @Override
            public Map<String, FileDataPart> getByteData() {
                Map<String, FileDataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new FileDataPart(imagename + ".jpg", image));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Metadata.requests.add(request);
        return future;
    }

    public static Future<NetworkResponse> getImage(int imageId) {
        String url = String.format(Locale.US, "https://%s/draw?id=%d", Metadata.serverURL, imageId);
        RequestFuture<NetworkResponse> future = RequestFuture.newFuture();
        FileUploadRequest request = new FileUploadRequest(Request.Method.GET, url, future, future);
        request.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );
        Metadata.requests.add(request);
        return future;
    }
}
