package com.example.bombsubmarinev2;

import android.util.Log;

/**
 * Created by 刘皓杰 on 2018/2/24.
 */

public class BombBoatThread extends Thread {
    boolean isRun;
    EnemyBomb[] enemyBombs;
    Boat boat;

    public BombBoatThread(EnemyBomb[] enemyBombs, Boat boat) {
        this.enemyBombs = enemyBombs;
        this.boat = boat;
        isRun = false;
    }

    public void setFlag(boolean flag) {
        isRun = flag;
    }

    @Override
    public void run() {
        try {
            while (isRun) {
                for (int i = 0; i < 6; i++) {
                    if (enemyBombs[i] != null) {
                        if (isCollision(enemyBombs[i].x, enemyBombs[i].y, enemyBombs[i].bmpEnemyBomb.getWidth(), enemyBombs[i].bmpEnemyBomb.getHeight(),
                                boat.X, boat.Y, boat.boat.getWidth(), boat.boat.getHeight())) {
//                            Boat.LIFE = false;
                            Boat.LIFE--;
                            Log.i("Life",Boat.LIFE+"");
                            //把敌人的炸弹放在屏幕外，不然会判定在一直炸玩家
                            enemyBombs[i].x=-200;
                            enemyBombs[i].y=-200;
                            enemyBombs[i].canBeBorn = true;
                        }
                    }
                }
                sleep(300);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isCollision(float x1, float y1, int w1, int h1, float x2, float y2, int w2, int h2) {
        if (x1 > x2 && x1 >= x2 + w2) {
            return false;
        } else if (x1 <= x2 && x1 + w1 <= x2) {
            return false;
        } else if (y1 >= y2 && y1 >= y2 + h2) {
            return false;
        } else if (y1 < y2 && y1 + h1 <= y2) {
            return false;
        }
        return true;
    }
}
