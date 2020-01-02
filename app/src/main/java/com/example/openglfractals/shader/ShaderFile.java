package com.example.openglfractals.shader;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderFile {
    private final Context context;
    private final int resourceId;

    public ShaderFile(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    public String content() {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't load resource");
        }
        return stringBuilder.toString();
    }
}
