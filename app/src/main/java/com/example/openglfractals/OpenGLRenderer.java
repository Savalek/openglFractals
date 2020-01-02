package com.example.openglfractals;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.example.openglfractals.texture.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform2f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class OpenGLRenderer implements Renderer {
    private final Context context;
    private final Camera camera;

    private FloatBuffer vertexData;
    private int programId;

    OpenGLRenderer(Context context) {
        this.context = context;
        float scale = 0.0025f;
        this.camera = new Camera(1000 * scale, 500 * scale, scale);
        prepareData();
    }

    private void prepareData() {
        float[] vertices = {
                -1, -1,
                -1, 1,
                1, 1,
                1, -1,
        };
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        OpenGLProgram openGLProgram = new OpenGLProgram(context, R.raw.vertex_shader, R.raw.fragment_shader);
        openGLProgram.start();
        glUseProgram(openGLProgram.id());
        bindData(openGLProgram.id());
        this.programId = openGLProgram.id();
    }

    private void bindData(int programId) {
        int aPositionLocation = glGetAttribLocation(programId, "a_Position");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // помещаем текстуру в target 2D юнита 0
        Texture texture = new Texture(context, R.raw.texture2);
        texture.loadTexture();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.id());

        // юнит текстуры
        int uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        glUniform1i(uTextureUnitLocation, 0);
    }

    private void updateCameraPosition() {
        int uScreeSizeLocation = glGetUniformLocation(programId, "u_Offset");
        glUniform2f(uScreeSizeLocation, camera.xOffset(), camera.yOffset());

        int uScaleSizeLocation = glGetUniformLocation(programId, "u_Scale");
        glUniform1f(uScaleSizeLocation, camera.scale());
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        updateCameraPosition();
    }

    void moveCamera(float xPixelDelta, float yPixelDelta) {
        float k = camera.scale() * 0.0168f;
        camera.applyXDelta(xPixelDelta * k);
        camera.applyYDelta(-yPixelDelta * k);
    }

    void cameraTempScale(float scaleFactor) {
        Log.i("scale", "scale: " + scaleFactor + "| camera: " + camera.scale());
        camera.setTempScaleDelta((float) (1.0 / scaleFactor));
        int coof = scaleFactor > 1 ? 1 : -1;
        moveCamera( - coof * 450, coof * 300);
    }

    void applyTempScaleDelta() {
        camera.applyTempScaleDelta();
    }
}