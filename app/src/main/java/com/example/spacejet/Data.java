package com.example.spacejet;

import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Data {
    static final int BULLETNUM = 10;
    static final int ENEMYNUM = 16;
    static final int ENEMYBULLETNUM = 16;
    static final int MENUBTNNUM = 5;
    static float viewWidth;
    static float viewHeight;
    static BTN[] menuBtns = new BTN[5];
    static EnemyBullet[] enemyBulletSet = new EnemyBullet[ENEMYBULLETNUM];
    static Enemy[] enemySet = new Enemy[ENEMYNUM];
    static Bullet[] bulletSet = new Bullet[BULLETNUM];
    static Player player;
    static int playerLife = -1;
    static JoyStick joyStick;
    static Boolean flag = false;
    static Random random = new Random();
    static Item item;
    static float touchX, touchY;
    static int selectedBtn = -1;
    static BTN pause;
    static Boolean isRuning = true;



    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) sqrt((pow((x1 - x2), 2) + pow((y1 - y2), 2)));
    }
}
