package club.psychose.luna.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class JsonUtils {
    // Method to fetch the JsonObject.
    public static JsonObject fetchOnlineJsonObject (String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        int line;
        while ((line = bufferedReader.read()) != -1) {
            stringBuilder.append((char) line);
        }

        return new Gson().fromJson(stringBuilder.toString().trim(), JsonObject.class);
    }
}