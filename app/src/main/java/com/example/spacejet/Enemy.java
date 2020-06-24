package com.example.spacejet;

import android.graphics.Bitmap;

import java.util.Random;

public class Enemy {
    boolean isDestory;
    int boom;
    float xPos;
    float yPos;
    float xDst;
    float speed;
    //贴图的宽高
    float width;
    float height;
    Bitmap enemyJetBitmap;

    Enemy(Bitmap bitmap) {
        boom = 0;
        isDestory = false;


        enemyJetBitmap = bitmap;
        width = enemyJetBitmap.getWidth();
        height = enemyJetBitmap.getHeight();
        yPos = -Data.random.nextInt((int) (Data.viewHeight - height) / 2);
        xPos = Data.random.nextInt((int) (Data.viewWidth - width));
        xDst = Data.random.nextInt((int) (Data.viewWidth - width));
        int temp = Data.random.nextInt(11);
        speed = temp < 5 ? 6 : temp;
    }
}
