package com.example.picshare.service;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.picshare.Metadata;
import com.example.picshare.domain.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatService {
    public static void sendMessage(List<User> subscribers, int imageId) {
        String url = String.format("https://%s/chat", Metadata.serverURL);
        for (User subscriber : subscribers) {
            Map<String, Integer> params = new HashMap<>();
            params.put("sender_id", Metadata.INSTANCE.getThisUser().getId());
            params.put("receiver_id",subscriber.getId());
            params.put("image_id", imageId);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                    System.out::println, System.out::println);
            Metadata.requests.add(request);
        }
    }
}
