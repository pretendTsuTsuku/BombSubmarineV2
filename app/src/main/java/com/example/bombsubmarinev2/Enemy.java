package com.example.bombsubmarinev2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by 刘皓杰 on 2018/2/21.
 */

public class  Enemy {


    Bitmap enemy;
    Paint paint;

    boolean isAlive;
    boolean canBeCollided;
    float x;
    float y;
    int speed;


    public Enemy(Bitmap enemy,float x,float y,int speed)
    {
        this.enemy=enemy;
        this.x=x;
        this.y=y;
        this.speed=speed;
        isAlive=true;
        canBeCollided=true;

        paint=new Paint();
        paint.setAntiAlias(true);
    }

    public void drawSelf(Canvas c)
    {
        if(isAlive)
        {
            x=x+speed;
            c.drawBitmap(enemy,x,y,paint);
        }

    }
}
