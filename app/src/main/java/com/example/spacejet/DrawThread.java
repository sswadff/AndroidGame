package com.example.spacejet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.SurfaceHolder;

import java.util.Date;
import java.util.HashMap;


public class DrawThread extends Thread {
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintA;
    Paint paintB;
    Paint paintC;
    Context context;
    Bitmap jet;
    Bitmap destory;
    Bitmap bulletPic;
    Bitmap enemyBullet;
    Bitmap itemBitmap;
    Bitmap failBitmap;
    Bitmap continueBitmap;
    Bitmap buttonGounp;
    Bitmap buttonGounpSelected;
    Bitmap menuBG;
    Bitmap pauseBitmap;
    int jetStata = 2;
    int frameSwitch = 0;
    int playerJetPicIndex;
    int bgCtrl = 0;
    Bitmap framJet[] = new Bitmap[6];
    Bitmap[] enemiesPic = new Bitmap[4];
    Bitmap[] destoryPicFrame = new Bitmap[6];
    Bitmap[] enemyBulletFrame = new Bitmap[2];
    Bitmap[] bulletFrame = new Bitmap[2];
    Bitmap[] itemsFrame = new Bitmap[6];
    Bitmap[] backGround;
    Bitmap[] btnSelectedSet = new Bitmap[Data.MENUBTNNUM];
    Bitmap[] btnSet = new Bitmap[Data.MENUBTNNUM];
    long lastFireTime;
    long enemyLastFireTime;
    long fpsCtrl;
    MediaPlayer mediaPlayer;
    SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
    HashMap<Integer, Integer> soundMap = new HashMap<>();

    DrawThread(SurfaceHolder holder, Context context) {
        this.holder = holder;
        this.context = context;
        continueBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.continue_pic);
        failBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.battle_fail);
        jet = BitmapFactory.decodeResource(context.getResources(), R.drawable.jet);
        destory = BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyed);
        itemBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.item);
        bulletPic = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_a);
        enemyBullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_enemy_a);
        menuBG = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu1);
        buttonGounp = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu2);
        buttonGounpSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu3);
        pauseBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        backGround = new Bitmap[]{BitmapFactory.decodeResource(context.getResources(), R.drawable.bg00)
                , BitmapFactory.decodeResource(context.getResources(), R.drawable.bg01)};
        for (int i = 0; i < 2; i++) {
            bulletFrame[i] = Bitmap.createBitmap(bulletPic, i * (bulletPic.getWidth() / 2), 0, (bulletPic.getWidth() / 2), bulletPic.getHeight());
            enemyBulletFrame[i] = Bitmap.createBitmap(enemyBullet, i * (enemyBullet.getWidth() / 2), 0, (enemyBullet.getWidth() / 2), enemyBullet.getHeight());
        }
        for (int i = 0; i < Data.MENUBTNNUM; i++) {
            btnSet[i] = Bitmap.createBitmap(buttonGounp, 0, i * (buttonGounp.getHeight() / Data.MENUBTNNUM), buttonGounp.getWidth(), (buttonGounp.getHeight() / Data.MENUBTNNUM));
            btnSelectedSet[i] = Bitmap.createBitmap(buttonGounpSelected, 0, i * (buttonGounpSelected.getHeight() / Data.MENUBTNNUM), buttonGounpSelected.getWidth(), (buttonGounpSelected.getHeight() / Data.MENUBTNNUM));
            Data.menuBtns[i] = new BTN(btnSet[i]);
        }
        Data.pause = new BTN(pauseBitmap);
        Data.pause.updateXPos(Data.viewWidth - Data.pause.width);
        enemiesPic[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy0);
        enemiesPic[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
        enemiesPic[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
        enemiesPic[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
        Data.player = new Player(jet);
        for (int i = 0; i < 6; i++) {
            itemsFrame[i] = Bitmap.createBitmap(itemBitmap, i * (itemBitmap.getWidth() / 6), 0, (itemBitmap.getWidth() / 6), itemBitmap.getHeight());
            framJet[i] = Bitmap.createBitmap(jet, i * (int) Data.player.width, 0, (int) Data.player.width, (int) Data.player.height);
            destoryPicFrame[i] = Bitmap.createBitmap(destory, i * (destory.getWidth() / 6), 0, (destory.getWidth() / 6), destory.getHeight());
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.game);
        soundMap.put(0, soundPool.load(context, R.raw.explosion, 1));
        soundMap.put(1, soundPool.load(context, R.raw.pickupitem, 1));
        Data.random.setSeed(new Date().getTime());
    }

    @Override
    public void run() {
        super.run();
        paintA = new Paint();
        paintB = new Paint();
        paintC = new Paint();
        paintA.setColor(Color.WHITE);
        paintC.setColor(Color.WHITE);
        paintB.setColor(Color.GRAY);
        paintA.setAntiAlias(true);
        paintB.setAntiAlias(true);
        paintC.setAntiAlias(true);
        paintB.setAlpha(75);
        paintC.setAlpha(50);
        lastFireTime = new Date().getTime();
        enemyLastFireTime = new Date().getTime();
        fpsCtrl = new Date().getTime();
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.5f, 0.5f);
        //此处做菜单与游戏切换
        while (!Data.flag && Data.isRuning) {
            try {
                btnOnClick();
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                menuDraw();
                Thread.sleep(15);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }//暂停
        //此处控制逻辑
        Data.playerLife = 2;
        while (Data.flag && Data.isRuning) {
            if (!Data.player.isDestory) {
                playerStatusUpdate();
                enemyStatusUpdate();
                bulletCtrl();
                enemyBulletCtrl();
                itemStatusUpdate();
                btnOnClick();
                while (!Data.flag && Data.isRuning) {
                    try {
                        btnOnClick();
                        canvas = holder.lockCanvas();
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        menuDraw();
                        Thread.sleep(15);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

                //随时控制Data.flag的值控制游戏进行
            } else {
                enemyStatusUpdate();
                bulletCtrl();
                enemyBulletCtrl();
                itemStatusUpdate();
            }
            //此处画画
            if (new Date().getTime() - fpsCtrl > 80) {
                frameSwitch = (frameSwitch + 1) % 2;
                fpsCtrl = new Date().getTime();
            }
            try {
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//
                backGroundDraw();
                joyStickDraw();
                enemiesDraw();
                itemDraw();
                bulletDraw();
                enemyBulletDraw();
                playerJetDraw();
                markDraw();
                Thread.sleep(15);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    //画画函数
    void playerJetDraw() {
        if (Data.player.isDestory) {
            if (Data.player.boom < 6) {
                if (Data.player.boom == 5) {
                    playBoom();
                }
                canvas.drawBitmap(destoryPicFrame[Data.player.boom], Data.player.xPos, Data.player.yPos, paintA);
                Data.player.boom++;
            } else {
                if (Data.playerLife == -1) {
                    Data.player = new Player(jet);
                    Data.playerLife = 3;
                    Data.enemySet = new Enemy[Data.ENEMYNUM];
                    Data.enemyBulletSet = new EnemyBullet[Data.ENEMYBULLETNUM];
                    Data.bulletSet = new Bullet[Data.BULLETNUM];
                    Data.item = new Item(itemBitmap);
                    playItemPickUp();
                }
                if (Data.playerLife > 0) {
                    Data.player.respawn();
                    Data.playerLife--;
                } else {
                    //failBitmap
                    canvas.drawBitmap(failBitmap, (Data.viewWidth - failBitmap.getWidth()) / 2, (Data.viewHeight - failBitmap.getHeight()) / 2, paintA);
                    if (frameSwitch == 0) {
                        canvas.drawBitmap(continueBitmap, (Data.viewWidth - continueBitmap.getWidth()) / 2, (Data.viewHeight - continueBitmap.getHeight()) * 7 / 8, paintA);
                    }
                }

            }
        } else {
            if (Data.player.dx > 0.5) {
                jetStata = 4;
            } else if (Data.player.dx < -0.5) {
                jetStata = 0;
            } else {
                jetStata = 2;
            }
            playerJetPicIndex = (jetStata + frameSwitch);
            canvas.drawBitmap(framJet[playerJetPicIndex], Data.player.xPos, Data.player.yPos, paintA);

        }
    }

    void joyStickDraw() {
        if (Data.joyStick.ctrlZoneXpos == -1 || Data.joyStick.ctrlZoneYpos == -1) {
            return;
        } else {
            canvas.drawCircle(Data.joyStick.ctrlZoneXpos, Data.joyStick.ctrlZoneYpos, Data.joyStick.ctrlRadius, paintC);
            canvas.drawCircle(Data.joyStick.stickXpos, Data.joyStick.stickYpos, Data.joyStick.stickRadius, paintB);
        }
    }

    void enemiesDraw() {
        for (int i = 0; i < Data.ENEMYNUM; i++) {
            if (Data.enemySet[i] == null) {
                continue;
            } else {
                if (Data.enemySet[i].isDestory) {
                    if (Data.enemySet[i].boom < 6) {
                        canvas.drawBitmap(destoryPicFrame[Data.enemySet[i].boom], Data.enemySet[i].xPos, Data.enemySet[i].yPos, paintA);
                        Data.enemySet[i].boom++;
                    } else {
                        if (Data.enemySet[i].yPos > -Data.enemySet[i].height) {
                            playBoom();
                        }
                        Data.enemySet[i] = null;
                    }
                } else {
                    canvas.drawBitmap(Data.enemySet[i].enemyJetBitmap, Data.enemySet[i].xPos, Data.enemySet[i].yPos, paintA);
                }
            }
        }
    }

    void bulletDraw() {
        for (int i = 0; i < Data.BULLETNUM; i++) {
            if (Data.bulletSet[i] == null) {
                continue;
            } else {
                canvas.drawBitmap(bulletFrame[frameSwitch], Data.bulletSet[i].xPos, Data.bulletSet[i].yPos, paintA);
            }
        }
    }

    void markDraw() {
        paintA.setTextSize(48);
        canvas.drawText("Kill:" + Data.player.kill, 0, 48, paintA);
        canvas.drawText("Excaped:" + Data.player.excape, 0, 96, paintA);
        canvas.drawText("Life:" + Data.playerLife, 0, 144, paintA);
        canvas.drawText("Firerate:" + (1000f / Data.player.firePerTime) + "/s", 0, 192, paintA);
        canvas.drawBitmap(Data.pause.bitmap, Data.pause.xPos, Data.pause.yPos, paintA);
    }

    void enemyBulletDraw() {
        for (int i = 0; i < Data.ENEMYBULLETNUM; i++) {
            if (Data.enemyBulletSet[i] == null) {
                continue;
            } else {
                canvas.drawBitmap(enemyBulletFrame[frameSwitch], Data.enemyBulletSet[i].xPos, Data.enemyBulletSet[i].yPos, paintA);
            }
        }
    }

    void itemDraw() {
        if (Data.item != null) {
            canvas.drawBitmap(itemsFrame[frameSwitch + Data.item.itemId], Data.item.xPos, Data.item.yPos, paintA);
        }
    }

    void backGroundDraw() {
        float height = backGround[0].getHeight();
        int length = (int) (Data.viewHeight / height) + 2;
        for (int i = 0; i < length; i++) {
            canvas.drawBitmap(backGround[i % 2], 0, (((i) * height) + bgCtrl) % (Data.viewHeight + height) - height, paintA);
        }

        bgCtrl = (int) ((bgCtrl + 1) % Data.viewHeight);
    }

    void menuDraw() {
        canvas.drawBitmap(menuBG, (Data.viewWidth - menuBG.getWidth()) / 2, (Data.viewHeight - menuBG.getHeight()) / 2, paintA);
        for (int i = 0; i < Data.MENUBTNNUM; i++) {
            canvas.drawBitmap(Data.menuBtns[i].bitmap, Data.menuBtns[i].xPos, (float) ((Data.viewHeight - buttonGounp.getHeight() * 1.5) / 2 + Data.menuBtns[i].height * 1.5 * i), paintA);
            Data.menuBtns[i].updateYPos((float) ((Data.viewHeight - buttonGounp.getHeight() * 1.5) / 2 + Data.menuBtns[i].height * 1.5 * i));
        }
        switch (Data.selectedBtn) {
            case 0:
                Data.flag = true;
                if (Data.playerLife != -1) {
                    Data.playerLife = -1;
                    Data.player.isDestory = true;
                }
                mediaPlayer.start();
                break;
            case 1:
                Data.flag = true;
                mediaPlayer.start();
                break;
        }

    }

    //逻辑函数
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
        for (int i = 0; i < Data.ENEMYNUM; i++) {
            if (Data.enemySet[i] == null) {
                continue;
            } else {
                float aJetLeft = Data.enemySet[i].xPos, aJetRight = aJetLeft + Data.enemySet[i].width,
                        aJetTop = Data.enemySet[i].yPos, aJetBottom = aJetTop + Data.enemySet[i].height;
                float bJetLeft = Data.player.xPos, bJetRight = bJetLeft + Data.player.width,
                        bJetTop = Data.player.yPos, bJetBottom = bJetTop + Data.player.height;
                Boolean collide = ((bJetLeft <= aJetLeft && aJetLeft < bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                        ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                        ((bJetLeft <= aJetLeft && aJetLeft <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom)) ||
                        ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom));
                if (collide) {
                    Data.enemySet[i].isDestory = true;
                    Data.player.isDestory = true;
                }
            }
        }
    }

    void enemyStatusUpdate() {
        for (int i = 0; i < Data.ENEMYNUM; i++) {
            if (Data.enemySet[i] == null) {
                Data.enemySet[i] = new Enemy(enemiesPic[Data.random.nextInt(3)]);
            } else {
                if (Data.enemySet[i].isDestory) {
                    continue;
                } else {
                    if (Data.enemySet[i].yPos > Data.viewHeight) {
                        Data.player.excape++;
                        Data.enemySet[i] = null;
                        continue;
                    }
                    //enemymove
                    Data.enemySet[i].yPos += Data.enemySet[i].speed;

                    if (Math.abs(Data.enemySet[i].xPos - Data.enemySet[i].xDst) < Data.enemySet[i].speed) {
                        Data.enemySet[i].xDst = Data.random.nextInt((int) (Data.viewWidth - Data.enemySet[i].width));
                    } else {
                        Data.enemySet[i].xPos += Data.enemySet[i].speed * (Data.enemySet[i].xPos - Data.enemySet[i].xDst < 0 ? 1 : -1);
                    }
                    for (int j = 0; j < Data.ENEMYNUM; j++) {
                        if (i == j || Data.enemySet[j] == null || Data.enemySet[i].isDestory || Data.enemySet[j].isDestory) {
                            continue;
                        } else {
                            float aJetLeft = Data.enemySet[i].xPos, aJetRight = aJetLeft + Data.enemySet[i].width,
                                    aJetTop = Data.enemySet[i].yPos, aJetBottom = aJetTop + Data.enemySet[i].height;
                            float bJetLeft = Data.enemySet[j].xPos, bJetRight = bJetLeft + Data.enemySet[j].width,
                                    bJetTop = Data.enemySet[j].yPos, bJetBottom = bJetTop + Data.enemySet[j].height;
                            Boolean collide = ((bJetLeft <= aJetLeft && aJetLeft < bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                                    ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                                    ((bJetLeft <= aJetLeft && aJetLeft <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom)) ||
                                    ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom));
                            if (collide) {
                                Data.enemySet[i].isDestory = true;
                                Data.enemySet[j].isDestory = true;
                            }
                        }
                    }
                }
            }
        }
    }

    void bulletCtrl() {
        for (int i = 0; i < Data.BULLETNUM; i++) {
            if (Data.bulletSet[i] == null && !Data.player.isDestory) {
                if (new Date().getTime() - lastFireTime > Data.player.firePerTime) {
                    Data.bulletSet[i] = new Bullet(bulletPic);
                    lastFireTime = new Date().getTime();
                }
                continue;
            } else {
                if (Data.bulletSet[i] == null) {
                    continue;
                }
                Data.bulletSet[i].yPos -= Data.bulletSet[i].speed;
                if (Data.bulletSet[i].yPos < -Data.bulletSet[i].height) {
                    Data.bulletSet[i] = null;
                    continue;
                }
                for (int j = 0; j < Data.ENEMYNUM; j++) {
                    if (Data.enemySet[j] == null) {
                        continue;
                    }
                    float aJetLeft = Data.bulletSet[i].xPos, aJetRight = aJetLeft + Data.bulletSet[i].width,
                            aJetTop = Data.bulletSet[i].yPos, aJetBottom = aJetTop + Data.bulletSet[i].height;
                    float bJetLeft = Data.enemySet[j].xPos, bJetRight = bJetLeft + Data.enemySet[j].width,
                            bJetTop = Data.enemySet[j].yPos, bJetBottom = bJetTop + Data.enemySet[j].height;
                    Boolean collide = ((bJetLeft <= aJetLeft && aJetLeft < bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                            ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                            ((bJetLeft <= aJetLeft && aJetLeft <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom)) ||
                            ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom));
                    if (collide) {
                        Data.bulletSet[i] = null;
                        Data.enemySet[j].isDestory = true;
                        Data.player.kill++;
                        return;
                    }
                }
            }

        }
    }

    void enemyBulletCtrl() {
        for (int i = 0; i < Data.ENEMYBULLETNUM; i++) {
            if (Data.enemyBulletSet[i] == null) {
                if (new Date().getTime() - enemyLastFireTime > 1000) {
                    Data.enemyBulletSet[i] = new EnemyBullet(enemyBullet);
                    enemyLastFireTime = new Date().getTime();
                }
                continue;
            } else {
                Data.enemyBulletSet[i].yPos += Data.enemyBulletSet[i].speed;
                if (Data.enemyBulletSet[i].yPos > Data.viewHeight) {
                    Data.enemyBulletSet[i] = null;
                    continue;
                }
                //----------------------------------------------------

                float aJetLeft = Data.enemyBulletSet[i].xPos, aJetRight = aJetLeft + Data.enemyBulletSet[i].width,
                        aJetTop = Data.enemyBulletSet[i].yPos, aJetBottom = aJetTop + Data.enemyBulletSet[i].height;
                float bJetLeft = Data.player.xPos, bJetRight = bJetLeft + Data.player.width,
                        bJetTop = Data.player.yPos, bJetBottom = bJetTop + Data.player.height;
                Boolean collide = ((bJetLeft <= aJetLeft && aJetLeft < bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                        ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                        ((bJetLeft <= aJetLeft && aJetLeft <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom)) ||
                        ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom));
                if (collide && !Data.player.isDestory) {
                    Data.player.isDestory = true;
                    Data.enemyBulletSet[i] = null;
                    continue;
                }
                //----------------------------------------------------------------------
            }
        }
    }

    void playBoom() {
        soundPool.play(soundMap.get(0), 0.5f, 0.5f, 1, 0, 1f);
    }

    void playItemPickUp() {
        soundPool.play(soundMap.get(1), 1f, 1f, 1, 0, 1f);
    }

    void itemStatusUpdate() {
        if (Data.item == null) {
            Data.item = new Item(itemBitmap);
            //record
        } else {
            //move
            Data.item.yPos += Data.item.speed;
            if (Data.item.yPos > Data.viewHeight) {
                Data.item = null;
                //record
                return;
            }
            if (Math.abs(Data.item.xPos - Data.item.xDst) < Data.item.speed) {
                Data.item.xDst = Data.random.nextInt((int) (Data.viewWidth - Data.item.width));
            } else {
                Data.item.xPos += Data.item.speed * (Data.item.xPos - Data.item.xDst < 0 ? 1 : -1);
            }
            //
            float aJetLeft = Data.item.xPos, aJetRight = aJetLeft + Data.item.width,
                    aJetTop = Data.item.yPos, aJetBottom = aJetTop + Data.item.height;
            float bJetLeft = Data.player.xPos, bJetRight = bJetLeft + Data.player.width,
                    bJetTop = Data.player.yPos, bJetBottom = bJetTop + Data.player.height;
            Boolean collide = ((bJetLeft <= aJetLeft && aJetLeft < bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                    ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetTop && aJetTop <= bJetBottom)) ||
                    ((bJetLeft <= aJetLeft && aJetLeft <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom)) ||
                    ((bJetLeft <= aJetRight && aJetRight <= bJetRight) && (bJetTop <= aJetBottom && aJetBottom <= bJetBottom));
            if (collide && !Data.player.isDestory) {
                switch (Data.item.itemId) {
                    case 0:
                        for (int i = 0; i < Data.ENEMYNUM; i++) {
                            if (Data.enemySet[i] == null || Data.enemySet[i].isDestory || Data.enemySet[i].yPos > Data.viewHeight || Data.enemySet[i].yPos + Data.enemySet[i].height < 0) {
                                continue;
                            }
                            Data.enemySet[i].isDestory = true;
                            Data.player.kill++;
                        }
                        Data.enemyBulletSet = new EnemyBullet[Data.ENEMYBULLETNUM];
                        break;
                    case 2:
                        Data.player.firePerTime /= 1.05f;
                        if (Data.player.firePerTime < 250) {
                            Data.player.firePerTime = 250;
                        }
                        break;
                    case 4:
                        Data.playerLife++;
                        break;
                }
                playItemPickUp();
                Data.item = null;
            }
        }

    }

    void btnOnClick() {
        if (!Data.flag) {
            for (int i = 0; i < Data.MENUBTNNUM; i++) {
                if ((Data.menuBtns[i].xPos < Data.touchX && Data.touchX < Data.menuBtns[i].rightPos) && (Data.menuBtns[i].yPos < Data.touchY && Data.touchY < Data.menuBtns[i].bottomPos)) {
                    playItemPickUp();
                    if (Data.selectedBtn != -1) {
                        Data.menuBtns[Data.selectedBtn].replaceBitmap(btnSet[Data.selectedBtn]);
                    }
                    Data.menuBtns[i].replaceBitmap(btnSelectedSet[i]);
                    Data.selectedBtn = i;
                    Data.touchX = -1;
                    Data.touchY = -1;
                }
            }
        } else {
            if ((Data.pause.xPos < Data.touchX && Data.touchX < Data.pause.rightPos) && (Data.pause.yPos < Data.touchY && Data.touchY < Data.pause.bottomPos)) {
                playItemPickUp();
                Data.flag = false;
                if (Data.selectedBtn != -1) {
                    Data.menuBtns[Data.selectedBtn].replaceBitmap(btnSet[Data.selectedBtn]);
                }
                mediaPlayer.pause();
                Data.selectedBtn = -1;
            }
        }
    }
}
