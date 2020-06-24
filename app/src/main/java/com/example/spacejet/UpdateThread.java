package com.example.spacejet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Date;

public class UpdateThread extends Thread {
    Context context;
    Bitmap[] enemiesPic = new Bitmap[4];

    UpdateThread(Context context) {
        this.context = context;
        enemiesPic[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy0);
        enemiesPic[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
        enemiesPic[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        enemiesPic[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
        Data.random.setSeed(new Date().getTime());
    }

    @Override
    public void run() {
        super.run();
        while (Data.flag) {
            try {
                if (!Data.player.isDestory) {
                    playerStatusUpdate();
                    Thread.sleep(30);
                } else {


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void playerStatusUpdate() {
        Data.player.xPos += Data.player.dx * Data.player.speed;
        Data.player.yPos += Data.player.dy * Data.player.speed;
        if (Data.player.yPos <= 0) {
            Data.player.yPos = 0;
        }
        if (Data.player.yPos + Data.player.height > Data.viewHeight) {
            Data.player.yPos = Data.viewHeight - Data.player.height;
        }
        if (Data.player.xPos <= 0) {
            Data.player.xPos = 0;
        }
        if (Data.player.xPos + Data.player.width > Data.viewWidth) {
            Data.player.xPos = Data.viewWidth - Data.player.width;
        }

    }

    void enemyStatusUpdate() {
        for (int i = 0; i < Data.ENEMYNUM; i++) {
            if (Data.enemySet[i] == null || Data.enemySet[i].isDestory) {
                Data.enemySet[i] = new Enemy(enemiesPic[Data.random.nextInt(3)]);
            }
        }
    }
}
