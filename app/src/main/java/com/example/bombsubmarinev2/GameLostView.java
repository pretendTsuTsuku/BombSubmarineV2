package com.example.bombsubmarinev2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 刘皓杰 on 2018/2/24.
 */

public class GameLostView extends SurfaceView implements SurfaceHolder.Callback {
    MainActivity mainActivity;

    Bitmap background;
    Bitmap btnReturn;
    Bitmap btnReturn_press;
    Bitmap btnRestart;
    Bitmap btnRestart_press;
    Canvas c;
    Paint paint;

    boolean isRun;
    boolean restartPress;
    boolean returnPress;

    SurfaceHolder holder;

    public GameLostView(MainActivity mainActivity) {
        super(mainActivity);
        this.mainActivity=mainActivity;
        holder=getHolder();
        holder.addCallback(this);
        paint=new Paint();
        restartPress=false;
        returnPress=false;
        isRun=true;
        initBitmap();
    }
    public void initBitmap()
    {
        background= BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.gameoverview);
        btnReturn =BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.return_button);
        btnReturn_press =BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.return_button_press);
        btnRestart=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.restartbutton);
        btnRestart_press=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.restartbutton_press);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRun=false;
    }

    public void doDraw(Canvas c)
    {
        //保存一下画布
        c.save();
        //根据屏幕按照比例放大画布
        c.scale((float) mainActivity.getScreenW()/background.getWidth(),(float)mainActivity.getScreenH()/background.getHeight());
        c.drawBitmap(background,0,0,paint);
        //画按钮不需要该比例的画布，所以移除掉
        c.restore();
        if(!returnPress)
        {
            c.drawBitmap(btnReturn,1100,800,paint);
        }
        else c.drawBitmap(btnReturn_press,1100,800,paint);
        if(!restartPress)
        {
            c.drawBitmap(btnRestart,300,800,paint);
        }
        else c.drawBitmap(btnRestart_press,300,800,paint);
    }

    Thread drawThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while (isRun)
            {
                try {
                    c=holder.lockCanvas();
                    synchronized (holder)
                    {
                        if(c!=null)
                        {
                            doDraw(c);
                        }
                        Thread.sleep(33);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                } finally {
                    if(c!=null)
                    {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    });

}
