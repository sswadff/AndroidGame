package com.example.spacejet;


import android.graphics.Bitmap;

public class Player {
    Player(Bitmap bitmap) {
        kill = 0;
        excape = 0;
        boom = 0;
        width = bitmap.getWidth() / 6;
        height = bitmap.getHeight();
        isDestory = false;
        xPos = (Data.viewWidth - width) / 2;
        yPos = (float) (Data.viewHeight - height * 1.25);
        speed = 18;
        firePerTime = 500;
        dx = 0;
        dy = 0;
    }

    void respawn() {
        isDestory = false;
        xPos = (Data.viewWidth - width) / 2;
        yPos = (float) (Data.viewHeight - height * 1.25);
        boom = 0;
    }

    int kill;
    int excape;
    int boom;
    float xPos;
    float yPos;
    float width;
    float height;
    float firePerTime;
    float dx;
    float dy;
    float speed;
    boolean isDestory;
}
