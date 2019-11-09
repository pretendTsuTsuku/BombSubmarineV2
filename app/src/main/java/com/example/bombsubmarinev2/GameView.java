package com.example.bombsubmarinev2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**
 * Created by 刘皓杰 on 2018/2/18.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback,View.OnTouchListener{

    MainActivity mainActivity;
    GameViewDrawThread gameViewDrawThread;
    SurfaceHolder holder;
    CollisionCheckThread collisionCheckThread;
    BombBoatThread bombBoatThread;



    @SuppressLint("HandlerLeak")
    /**
     * 收到消息就处理一个Enemy
     */
            Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            float x;
            float y;
            x=-(float)bmpEnemy.getWidth()-5;
            float randomY;
            randomY=new Random().nextFloat();
            y=randomY*(mainActivity.getScreenH()*3/4)+mainActivity.getScreenH()/4-bmpEnemy.getHeight();
            int speed=new Random().nextInt(4)+1;
            if(enemy[msg.what]==null)
            {
                enemy[msg.what]=new Enemy(bmpEnemy,x,y,speed);
            }
            else {
                enemy[msg.what].x=x;
                enemy[msg.what].y=y;
                enemy[msg.what].speed=speed;
                enemy[msg.what].isAlive=true;
                enemy[msg.what].canBeCollided=true;
            }
            if(enemyBombs[msg.what]==null)
            {
                enemyBombs[msg.what]=new EnemyBomb(enemy[msg.what],bmpEnemyBomb);//当潜艇可用时炸弹也可用
            }
            super.handleMessage(msg);
        }
    };

    static boolean isAlive=false;//标识这个View是否还活着，停止检测线程

    /**
     * 初始化6个Enemy
     */
    Thread t=new Thread(new Runnable() {
        @Override
        public void run() {
            int count=0;
            while (count<6)
            {
                for(int i=0;i<6;i++)
                {
                    if(enemy[i]==null)
                    {
                        handler.sendEmptyMessage(i);
                        count++;
                        break;
                    }
                }
                try {
                    //随机休眠5到9秒
                    Thread.sleep(new Random().nextInt(5000)+4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });




    Paint paint;
    Paint textPaint;

    Boat boat;//1个玩家
    Enemy[] enemy=new Enemy[6];//6个敌人
    Bomb[] bomb=new Bomb[3];//3枚炸弹
    EnemyBomb[] enemyBombs=new EnemyBomb[6];//每个敌人绑定一个炸弹

    Bitmap gameBackground;
    Bitmap bmpEnemy;
    Bitmap bmpEnemyBomb;
    Bitmap bmpBombB;
    Bitmap bmpEnemyCrash;


    public GameView(final MainActivity mainActivity) {
        super(mainActivity);
        this.mainActivity=mainActivity;
        boat=new Boat(mainActivity);    //初始化玩家
        holder=getHolder();
        holder.addCallback(this);
        setOnTouchListener(this);

        gameViewDrawThread=new GameViewDrawThread(this,holder);

        isAlive=true;
        textPaint=new Paint();
        textPaint.setTextSize(40);
        paint=new Paint();
        paint.setAntiAlias(true);

        gameBackground= BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.background);
        bmpEnemy=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.submarine);
        bmpBombB=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.bomb);
        bmpEnemyBomb=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.bombe);
        bmpEnemyCrash=BitmapFactory.decodeResource(mainActivity.getResources(),R.drawable.enemyboom);

        //准备好玩家炸弹
        for(int i=0;i<3;i++)
        {
            bomb[i]=new Bomb(bmpBombB);
        }

        t.start();    //开始检测空Enemy
    }

    public void doDraw(Canvas c) {
        c.drawBitmap(gameBackground, 0, 0, paint);//画游戏背景
        c.drawText("消灭敌人："+CollisionCheckThread.SCORE,0,55,textPaint);
        c.drawText("生命值:"+Boat.LIFE,600,55,textPaint);
        c.drawText("剩余任务数："+CollisionCheckThread.MISSION,mainActivity.getScreenW()-290,55,textPaint);
        boat.drawSelf(c);                               //画船
        /**
         * 画敌人
         */
        for (int j = 0; j < 6; j++) {
            if (enemy[j] != null) {
                enemy[j].drawSelf(c);
                //如果有Enemy越界或被炸了  2秒后重新回到起点
                if (enemy[j].x > mainActivity.getScreenW())
                {
                    handler.sendEmptyMessageDelayed(j,2000);
                }
                if (!enemy[j].isAlive)
                {
                    //画爆炸图片
                    c.drawBitmap(bmpEnemyCrash,enemy[j].x,enemy[j].y-10,paint);
                    handler.sendEmptyMessageDelayed(j,1000);
                }
            }
        }

        /**
         * 画炸弹
         */
        for(int i=0;i<3;i++)
        {
            if(bomb[i].isAlive)            //如果有炸弹被激活了
            {
                bomb[i].drawSelf(c);
                if(bomb[i].y>mainActivity.getScreenH())
                {
                    bomb[i].isAlive=false; //越界就算炸弹失效
                }

            }
        }
        /**
         * 敌方炸弹
         */
        for(int j=0;j<6;j++)
        {
            if(enemyBombs[j]!=null)
            {
                if(enemyBombs[j].canBeDraw())      //如果炸弹可被画
                {
                    if(bombBoatThread==null)           //启动炸船检测线程，只执行一次就行了
                    {
                        bombBoatThread=new BombBoatThread(enemyBombs,boat);
                        bombBoatThread.setFlag(true);
                        bombBoatThread.start();
                    }
                    enemyBombs[j].drawSelf(c);
                    if(enemyBombs[j].y<(float) 0-enemyBombs[j].bmpEnemyBomb.getHeight())//如果炸弹越界
                    {
                        enemyBombs[j].canBeBorn=true;//越界就可以再次被生成
                    }
                }
            }

        }
    }





    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameViewDrawThread.setFlag(true);
        gameViewDrawThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isAlive=false;
        gameViewDrawThread.setFlag(false);
        if(collisionCheckThread!=null){
            collisionCheckThread.setFlag(false);
        }
        if(bombBoatThread!=null){
            bombBoatThread.setFlag(false);
        }
    }


    /**
     * 处理点击事件
     * @param view
     * @param motionEvent
     * @return false
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(collisionCheckThread==null)
        {
            collisionCheckThread=new CollisionCheckThread(enemy,bomb);
            collisionCheckThread.setFlag(true);
            collisionCheckThread.start();
        }

        for(int i=0;i<3;i++)
        {
            if(!bomb[i].isAlive)//如果炸弹不可用
            {
                //赋予它们X，Y坐标并激活炸弹
                bomb[i].x= boat.X+110;
                bomb[i].y=(float) boat.boat.getHeight();
                bomb[i].isAlive=true;
                break;
            }
        }

        return false;//return false只接受Down事件
    }
}
