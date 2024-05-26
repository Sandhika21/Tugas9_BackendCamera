package com.example.api;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean uploadImage(String requestUrl, byte[] imageByteArray) {
        String imageBase64 = Base64.getEncoder().encodeToString(imageByteArray);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image", imageBase64);
        } catch (Exception e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            return false;
        }

        HttpURLConnection conn = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            try (OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream())) {
                outputStream.write(jsonObject.toString());
                outputStream.flush();
            }

            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
            return false;
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
