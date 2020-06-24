package com.example.spacejet;

import android.graphics.Bitmap;

public class Bullet {
    float xPos;
    float yPos;
    float width;
    float height;
    float speed;
    Bitmap bulletMap;
    boolean isHit;

    Bullet(Bitmap bitmap) {
        isHit = false;
        bulletMap = bitmap;
        width = bitmap.getWidth()/2;
        height = bitmap.getHeight();
        xPos = (Data.player.xPos + (Data.player.width - width)/ 2);
        yPos = (Data.player.yPos-50);
        speed = 40;
    }
}
