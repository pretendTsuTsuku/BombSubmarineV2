package com.example.bombsubmarinev2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by 刘皓杰 on 2018/2/24.
 */

public class EnemyBomb {

    boolean isAlive;
    boolean canBeBorn;

    Paint paint;
    Enemy enemy;

    float x;
    float y;
    Bitmap bmpEnemyBomb;
    public EnemyBomb(Enemy enemy, Bitmap bmpEnemyBomb)
    {
        this.enemy=enemy;
        this.bmpEnemyBomb=bmpEnemyBomb;
        paint=new Paint();
        paint.setAntiAlias(true);
        isAlive=false;
        canBeBorn=true;
        born.start();
    }

    public void drawSelf(Canvas c)
    {
        y=y-2;
        c.drawBitmap(bmpEnemyBomb,x,y,paint);
    }
    public Boolean canBeDraw()
    {
        return isAlive&&!canBeBorn;
    }

    Thread born=new Thread(new Runnable() {
        @Override
        public void run() {
            while (GameView.isAlive)
            {
                if(canBeBorn)
                {
                    x=enemy.x+100;
                    y=enemy.y-bmpEnemyBomb.getHeight();
                    isAlive=true;
                    canBeBorn=false;
                }
                try {
                    Thread.sleep(new Random().nextInt(4000)+7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    });
}
