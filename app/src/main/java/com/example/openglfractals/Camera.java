package com.example.openglfractals;

class Camera {
    private float xOffset;
    private float yOffset;
    private float scale;
    private float tempScaleDelta = 1;

    Camera(float xOffset, float yOffset, float scale) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.scale = scale;
    }

    void applyXDelta(float xDelta) {
        xOffset += xDelta;
    }

    void applyYDelta(float yDelta) {
        yOffset += yDelta;
    }

    void setTempScaleDelta(float tempScaleDelta) {
        this.tempScaleDelta = tempScaleDelta;
    }

    void applyTempScaleDelta() {
        scale *= tempScaleDelta;
        tempScaleDelta = 1;
    }

    float xOffset() {
        return xOffset;
    }

    float yOffset() {
        return yOffset;
    }

    float scale() {
        return scale * tempScaleDelta;
    }
}
