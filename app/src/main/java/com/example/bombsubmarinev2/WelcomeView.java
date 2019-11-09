package com.example.bombsubmarinev2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by 刘皓杰 on 2018/2/18.
 */

public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback{

    MainActivity mainActivity;
    WelcomeViewThread welcomeViewThread;
    SurfaceHolder holder;

    Bitmap background;
    Bitmap btnStart;
    Bitmap btnStart_press;
    Paint paint;

    boolean isPress = false;

    public WelcomeView(MainActivity mainActivity) {
        super(mainActivity);
        this.mainActivity = mainActivity;
        holder = getHolder();
        holder.addCallback(this);


        background = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.menu);
        btnStart = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.button);
        btnStart_press = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.button_press);
        paint = new Paint();
        paint.setAntiAlias(true);

        welcomeViewThread = new WelcomeViewThread(this, holder);
    }

    /**
     * 欢迎界面的绘画方法
     *
     * @param c 屏幕画布
     */
    public void doDraw(Canvas c) {
        //保存一下画布
        c.save();
        //根据屏幕按照比例放大画布
        c.scale((float) mainActivity.getScreenW() / background.getWidth(), (float) mainActivity.getScreenH() / background.getHeight());
        c.drawBitmap(background, 0, 0, paint);
        //画按钮不需要该比例的画布，所以移除掉
        c.restore();
        //按钮有两种状态所以区别开画
        if (!isPress) {
            c.drawBitmap(btnStart, 700, 800, paint);
        } else c.drawBitmap(btnStart_press, 700, 800, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        welcomeViewThread.setFlag(true);
        welcomeViewThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//        boolean retry=true;
//        welcomeViewThread.setFlag(false);
//        while (retry){
//            try {
//                welcomeViewThread.join();
//                retry=false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        welcomeViewThread.setFlag(false);
    }

}
