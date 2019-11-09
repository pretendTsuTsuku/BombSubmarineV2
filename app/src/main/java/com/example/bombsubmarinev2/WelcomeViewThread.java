package com.example.bombsubmarinev2;

import android.graphics.Canvas;
import android.speech.tts.SynthesisRequest;
import android.view.SurfaceHolder;

/**
 * Created by 刘皓杰 on 2018/2/18.
 */

public class WelcomeViewThread extends Thread {
    WelcomeView welcomeView;
    SurfaceHolder holder;
    Boolean isRun;
    Canvas c;


    public WelcomeViewThread(WelcomeView welcomeView,SurfaceHolder holder)
    {
        this.welcomeView=welcomeView;
        this.holder=holder;
        isRun=true;
    }

    public void setFlag(Boolean flag)
    {
        isRun=flag;
    }

    @Override
    public void run()
    {
        while (isRun)
        {
            try {
                c = holder.lockCanvas();
                synchronized (holder) {
                    if(c!=null)
                    {
                        welcomeView.doDraw(c);
                    }
                    sleep(33);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}
