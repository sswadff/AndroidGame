package com.example.spacejet;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    DisplayMetrics displayM;
    DrawThread drawThread;
    Context context;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        displayM = this.getResources().getDisplayMetrics();
        Data.viewWidth = displayM.widthPixels;
        Data.viewHeight = displayM.heightPixels;
        Data.joyStick = new JoyStick();
        drawThread = new DrawThread(mHolder, getContext());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();
        if (event.getAction() == event.ACTION_DOWN) {
            Data.touchX = touchX;
            Data.touchY = touchY;
            Data.joyStick.stickYpos = Data.joyStick.ctrlZoneYpos = touchY;
            Data.joyStick.stickXpos = Data.joyStick.ctrlZoneXpos = touchX;

            if (Data.playerLife == 0 && Data.player.isDestory) {
                Data.playerLife = -1;
            }

        } else if (event.getAction() == event.ACTION_UP) {
            Data.joyStick.stickYpos = Data.joyStick.ctrlZoneYpos = -1;
            Data.joyStick.stickXpos = Data.joyStick.ctrlZoneXpos = -1;
            Data.player.dx = 0;
            Data.player.dy = 0;
        } else {
            if (Data.joyStick.ctrlRadius >= Data.distance(Data.joyStick.ctrlZoneXpos, Data.joyStick.ctrlZoneYpos, touchX, touchY)) {
                Data.joyStick.stickXpos = touchX;
                Data.joyStick.stickYpos = touchY;
                Data.player.dx = (Data.joyStick.stickXpos - Data.joyStick.ctrlZoneXpos) / Data.joyStick.ctrlRadius;
                Data.player.dy = (Data.joyStick.stickYpos - Data.joyStick.ctrlZoneYpos) / Data.joyStick.ctrlRadius;
            } else {
                Data.player.dy = (touchY - Data.joyStick.ctrlZoneYpos) / Data.distance(Data.joyStick.ctrlZoneXpos, Data.joyStick.ctrlZoneYpos, touchX, touchY);
                Data.player.dx = (touchX - Data.joyStick.ctrlZoneXpos) / Data.distance(Data.joyStick.ctrlZoneXpos, Data.joyStick.ctrlZoneYpos, touchX, touchY);
                Data.joyStick.stickXpos = Data.joyStick.ctrlZoneXpos + Data.player.dx * Data.joyStick.ctrlRadius;
                Data.joyStick.stickYpos = Data.joyStick.ctrlZoneYpos + Data.player.dy * Data.joyStick.ctrlRadius;
            }
        }
        return true;
    }

}
