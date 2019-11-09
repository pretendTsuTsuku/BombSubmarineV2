package com.example.bombsubmarinev2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    View current;

    GameView gameView;
    WelcomeView welcomeView;
    GameLostView gameLostView;
    GameWinView gameWinView;


    final int BOATED = 1;
    final int ChangeGtoS=2;

    @SuppressLint("HandlerLeak")
    Handler changViewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BOATED:
                    setGameLostView();
                    break;
                case ChangeGtoS:
                    setGameWinView();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    Thread checkIsDead = new Thread(new Runnable() {
        @Override
        public void run() {
            while (GameView.isAlive) {
                if (Boat.LIFE==0) {
                    changViewHandler.sendEmptyMessage(BOATED);
                    break;
//                    Boat.LIFE = true;
                }
                if(CollisionCheckThread.MISSION==0)
                {
                    changViewHandler.sendEmptyMessage(ChangeGtoS);
                    break;
//                    CollisionCheckThread.MISSION++;//加一个数 不让它等于0了 否者会一直发送消息
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        welcomeView = new WelcomeView(this);
        setContentView(welcomeView);
        current = welcomeView;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:    //手指按下
                /**
                 * 如果是欢迎界面
                 */
                if (current == welcomeView) {
                    //按下开始游戏
                    if (x > 700.0 && x < 700.0 + welcomeView.btnStart.getWidth() && y > 800.0 && y < 800.0 + welcomeView.btnStart.getHeight()) {
                        welcomeView.isPress = true;
                    }
                }
                /**
                 * 游戏失败界面
                 */
                if(current==gameLostView){
                    //按下重新开始
                    if(x > 300.0 && x < 300.0 + gameLostView.btnRestart.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnRestart.getHeight()){
                        gameLostView.restartPress=true;
                    }
                    //按下回到主菜单
                    if(x > 1100.0 && x < 1100.0 + gameLostView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnReturn.getHeight()){
                        gameLostView.returnPress=true;
                    }
                }
                /**
                 * 游戏胜利界面
                 */
                if(current==gameWinView){
                    //按下回到主菜单
                    if (x > 700.0 && x < 700.0 + gameWinView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameWinView.btnReturn.getHeight()) {
                        gameWinView.returnPress = true;
                    }
                }


                break;
            case MotionEvent.ACTION_MOVE:    //手指移动
                /**
                 * 如果是欢迎界面
                 */
                if (current == welcomeView) {
                    if (!(x > 700.0 && x < 700.0 + welcomeView.btnStart.getWidth() && y > 800.0 && y < 800.0 + welcomeView.btnStart.getHeight())) {
                        //改变按钮的状态
                        welcomeView.isPress = false;
                    }
                }
                /**
                 * 游戏失败界面
                 */
                if(current == gameLostView) {
                    if(!(x > 300.0 && x < 300.0 + gameLostView.btnRestart.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnRestart.getHeight())) {
                        gameLostView.restartPress=false;
                    }
                    if(!(x > 1100.0 && x < 1100.0 + gameLostView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnReturn.getHeight())) {
                        gameLostView.returnPress=false;
                    }
                }
                /**
                 * 游戏胜利界面
                 */
                if(current==gameWinView){
                    if (!(x > 700.0 && x < 700.0 + gameWinView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameWinView.btnReturn.getHeight())) {
                        //改变按钮的状态
                        gameWinView.returnPress = false;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:     //手指抬起
                /**
                 * 如果是欢迎界面
                 */
                if (current == welcomeView) {
                    if (x > 700.0 && x < 700.0 + welcomeView.btnStart.getWidth() && y > 800.0 && y < 800.0 + welcomeView.btnStart.getHeight()) {
                        if (welcomeView.isPress) {
                            setGameView();
                        }
                    }
                }
                /**
                 * 游戏失败界面
                 */
                if(current==gameLostView) {
                    if(x > 300.0 && x < 300.0 + gameLostView.btnRestart.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnRestart.getHeight()){
                        if(gameLostView.restartPress) {
                            setGameView();
                        }
                    }
                    if(x > 1100.0 && x < 1100.0 + gameLostView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameLostView.btnReturn.getHeight()){
                        if(gameLostView.returnPress) {
                            setWelcomeView();
                        }
                    }
                }
                /**
                 * 游戏胜利界面
                 */
                if(current==gameWinView){
                    if (x > 700.0 && x < 700.0 + gameWinView.btnReturn.getWidth() && y > 800.0 && y < 800.0 + gameWinView.btnReturn.getHeight()) {
                        if(gameWinView.returnPress)
                        {
                            setWelcomeView();
                        }
                    }
                }

                break;
        }
        return false;
    }

    public void setGameView()
    {
        gameView = new GameView(this);
        setContentView(gameView);
        CollisionCheckThread.SCORE = 0;
        CollisionCheckThread.MISSION=30;
        Boat.LIFE=3;
        checkIsDead.start();
        current = gameView;
    }

    public void setWelcomeView() {
        welcomeView=new WelcomeView(this);
        setContentView(welcomeView);
        current=welcomeView;
    }

    public void setGameLostView() {
        gameLostView=new GameLostView(this);
        setContentView(gameLostView);
        current=gameLostView;
    }

    public void setGameWinView(){
        gameWinView=new GameWinView(this);
        setContentView(gameWinView);
        current=gameWinView;

    }

    long clickbacktime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(current==welcomeView)
            {
                if(System.currentTimeMillis()-clickbacktime>2000){
                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    clickbacktime=System.currentTimeMillis();
                }
                else {
                    finish();
                    System.exit(0);
                }
            }
            else setWelcomeView();
        }
        return true;
    }


    public int getScreenW() {
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public int getScreenH() {
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
