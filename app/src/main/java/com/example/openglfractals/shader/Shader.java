package com.example.openglfractals.shader;

import android.content.Context;

import static android.opengl.GLES20.*;

public class Shader {
    private final Context context;
    private final int type;
    private final String text;

    private int id;

    public Shader(Context context, int type, String text) {
        this.context = context;
        this.type = type;
        this.text = text;
    }

    public void register() {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new RuntimeException("Can't create shader");
        }
        glShaderSource(shaderId, text);
        glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderId);
            throw new RuntimeException("Can't compile shader");
        }
        id = shaderId;
    }

    public int id() {
        return id;
    }
}
