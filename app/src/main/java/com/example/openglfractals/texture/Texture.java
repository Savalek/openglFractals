package com.example.openglfractals.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;

public class Texture {

    private final Context context;
    private final int resourceId;

    private int id;

    public Texture(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    public void loadTexture() {
        // создание объекта текстуры
        final int[] textureIds = new int[1];
        glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            throw new RuntimeException("Can't create texture");
        }

        // получение Bitmap
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if (bitmap == null) {
            glDeleteTextures(1, textureIds, 0);
            throw new RuntimeException("Can't create bitmap for texture");
        }

        // настройка объекта текстуры
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureIds[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        // сброс target
        glBindTexture(GL_TEXTURE_2D, 0);

        this.id = textureIds[0];
    }

    public int id() {
        return id;
    }
}
