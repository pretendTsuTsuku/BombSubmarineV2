package com.example.bombsubmarinev2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by 刘皓杰 on 2018/2/21.
 */

public class Bomb {

    float x=800;
    float y;
    Paint paint;
    Bitmap bomb;

    boolean isAlive;


    public Bomb(Bitmap bomb)
    {
        paint=new Paint();
        paint.setAntiAlias(true);
        this.bomb=bomb;

        isAlive=false;

    }

    public void drawSelf(Canvas c)
    {
        if(isAlive) {
                y += 2;
                c.drawBitmap(bomb,x,y,paint);
        }
    }
}
