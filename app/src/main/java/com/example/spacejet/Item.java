package com.example.spacejet;

import android.graphics.Bitmap;

public class Item {
    public static int MISSILE = 0;
    public static int SHIELD = 2;
    public static int LIFE = 4;
    float xPos;
    float yPos;
    int itemId;
    float speed;
    float width;
    float height;
    float xDst;

    Item(Bitmap bitmap) {
        itemId = Data.random.nextInt(3) * 2;
        width = bitmap.getWidth() / 6;
        height = bitmap.getHeight();
        yPos = -Data.random.nextInt((int) (Data.viewHeight - height) / 2);
        xPos = Data.random.nextInt((int) (Data.viewWidth - width));
        xDst = Data.random.nextInt((int) (Data.viewWidth - width));
        speed = 3;
    }
}
