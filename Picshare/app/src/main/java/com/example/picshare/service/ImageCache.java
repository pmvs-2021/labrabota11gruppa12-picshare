package com.example.picshare.service;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCache {
    public static boolean contains(Context ctx, String imageId) {
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("cache", Context.MODE_PRIVATE);
        File f = new File(directory, imageId + ".jpg");
        return f.exists();
    }

    public static Bitmap downloadOrNull(Context ctx, String imageId) throws FileNotFoundException {
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("cache", Context.MODE_PRIVATE);
        File f = new File(directory, imageId + ".jpg");
        if (!f.exists()) {
            return null;
        }
        return BitmapFactory.decodeStream(new FileInputStream(f));
    }

    public static void upload(Context ctx, String imageId, Bitmap bmp) {
        ContextWrapper cw = new ContextWrapper(ctx);
        File directory = cw.getDir("cache", Context.MODE_PRIVATE);
        File imgPath = new File(directory, imageId + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imgPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } ;
    }
}
