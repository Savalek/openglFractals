package com.example.openglfractals;

import android.content.Context;

import com.example.openglfractals.shader.Shader;
import com.example.openglfractals.shader.ShaderFile;

import static android.opengl.GLES20.*;

class OpenGLProgram {
    private final Context context;
    private final int vertexShaderResourceId;
    private final int fragmentShaderResourceId;

    private int programId;

    OpenGLProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        this.context = context;
        this.vertexShaderResourceId = vertexShaderResourceId;
        this.fragmentShaderResourceId = fragmentShaderResourceId;
    }

    void start() {
        int programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Can't create openGL program");
        }

        attachVertexShader(programId);
        attachFragmentShader(programId);
        glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            throw new RuntimeException("Can't link program");
        }
        this.programId = programId;
    }

    private void attachVertexShader(int programId) {
        ShaderFile shaderFile = new ShaderFile(context, vertexShaderResourceId);
        Shader shader = new Shader(context, GL_VERTEX_SHADER, shaderFile.content());
        shader.register();
        glAttachShader(programId, shader.id());
    }

    private void attachFragmentShader(int programId) {
        ShaderFile shaderFile = new ShaderFile(context, fragmentShaderResourceId);
        Shader shader = new Shader(context, GL_FRAGMENT_SHADER, shaderFile.content());
        shader.register();
        glAttachShader(programId, shader.id());
    }

    int id() {
        return programId;
    }
}
