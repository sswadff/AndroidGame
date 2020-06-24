package com.example.spacejet;

import android.graphics.Bitmap;

public class BTN {
    float width;
    float height;
    float xPos;
    float yPos = 0;
    float rightPos;
    float bottomPos;
    Bitmap bitmap;

    BTN(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        xPos = (Data.viewWidth - width) / 2;
        rightPos = xPos + width;
        bottomPos = yPos + height;
        this.bitmap = bitmap;
    }

    void replaceBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        width = bitmap.getWidth();
        xPos = (Data.viewWidth - width) / 2;
        rightPos = xPos + width;
    }

    void updateYPos(float yPos) {
        this.yPos = yPos;
        bottomPos = yPos + height;
    }

    void updateXPos(float xPos) {
        this.xPos = xPos;
        rightPos = xPos + width;
    }
}
