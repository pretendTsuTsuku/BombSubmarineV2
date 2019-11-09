package com.example.bombsubmarinev2;

/**
 * Created by 刘皓杰 on 2018/2/22.
 */

public class CollisionCheckThread extends Thread {
    public static int SCORE = 0;
    public static int MISSION = 30;

    boolean isRun;

    Bomb[] bomb;
    Enemy[] enemy;

    public CollisionCheckThread(Enemy[] enemy, Bomb[] bomb) {
        this.enemy = enemy;
        this.bomb = bomb;
        isRun = false;
    }

    public void setFlag(boolean flag) {
        isRun = flag;
    }

    @Override
    public void run() {
        try {
            while (isRun) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (enemy[j] != null) {
                            if(enemy[j].canBeCollided){
                                if (isCollision(bomb[i].x, bomb[i].y, bomb[i].bomb.getWidth(), bomb[i].bomb.getHeight(),
                                        enemy[j].x, enemy[j].y, enemy[j].enemy.getWidth(), enemy[j].enemy.getHeight())) {
                                    SCORE++;
                                    MISSION--;
                                    bomb[i].isAlive = false;
                                    //把炸弹放在潜艇接触不到的地方
                                    bomb[i].x = 800;
                                    bomb[i].y = 0;
                                    //消失敌人并放在最左上角，不能让炸弹接触到否则会继续计分
                                    enemy[j].isAlive = false;
                                    enemy[j].canBeCollided=false;
//                                    enemy[j].x = 0;
//                                    enemy[j].y = 0;
                                }
                            }

                        }
                    }
                }
                sleep(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 碰撞检测
     *
     * @param x1 炸弹的x坐标
     * @param y1 炸弹的y坐标
     * @param w1 炸弹的宽
     * @param h1 炸弹的高
     * @param x2 潜艇的x坐标
     * @param y2 潜艇的y坐标
     * @param w2 潜艇的宽
     * @param h2 潜艇的高
     * @return true是碰撞  false非碰撞
     */

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
