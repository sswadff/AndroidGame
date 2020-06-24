package com.example.spacejet;

import android.graphics.Bitmap;

import java.util.Date;

public class EnemyBullet {
    float xPos;
    float yPos;
    float width;
    float height;
    float speed;
    Bitmap bulletMap;
    boolean isHit;
    int shooter;
    float timeOut;

    EnemyBullet(Bitmap bitmap) {
        isHit = false;
        bulletMap = bitmap;
        width = bitmap.getWidth() / 2;
        height = bitmap.getHeight();
        shooter = Data.random.nextInt(Data.ENEMYNUM);
        timeOut = new Date().getTime();
        while (Data.enemySet[shooter] == null || Data.enemySet[shooter].isDestory) {
            if (new Date().getTime() - timeOut > 500) {
                break;
            }
            shooter = Data.random.nextInt(Data.ENEMYNUM);
        }
        xPos = (Data.enemySet[shooter].xPos + (Data.enemySet[shooter].width - width) / 2);
        yPos = (Data.enemySet[shooter].yPos + Data.enemySet[shooter].height + 35);
        speed = 10;
    }

}
