package com.sorashiro.swipe.data;

import android.widget.RelativeLayout;

import com.sorashiro.swipe.R;
import com.sorashiro.swipe.StoreItem;

import java.util.Random;

/**
 * @author Sora
 * @date 2016/11/8
 *
 * 一些游戏的全局设置
 *
 */

public class Settings {

    //持有金币上限，2的29次方
    public static final int LIMIT_COINS = 536870912;

    //初始化倒计时进度条参数
    public static final int BEGIN_TIME              = 60;
    public static final int DECREASE_BY_SECOND      = 1;
    public static final int CORRECT_ADD_TIME        = 1;
    public static final int INCORRECT_DECREASE_TIME = 1;

    //Bonus名称
    public static final int DROP_NOTHING = 0;
    public static final int DROP_COINS = 1;
    public static final int DROP_TIME = 2;
    public static final int DROP_SCORE = 3;

    //Bonus的最高等级
    public static final int MAX_LEVEL = 5;
    //Items的最高数量
    public static final int MAX_ITEM_NUM = 99;

    //背景音乐文件名
    public static final int GAME_MUSIC = R.raw.swipe_game;


    private static final String UP    = "U";
    private static final String DOWN  = "D";
    private static final String LEFT  = "L";
    private static final String RIGHT = "R";

    //检查卡片运动趋势算法
    public static String checkOri(RelativeLayout.LayoutParams params,
                                  float mOriginalLeftMargin,
                                  float mOriginalTopMargin,
                                  int mPlanToSort) {
        String mOriStr = "";
        int leftMargin = params.leftMargin;
        int topMargin = params.topMargin;
        float deltaLeftMargin = leftMargin - mOriginalLeftMargin;
        float deltaTopMargin = topMargin - mOriginalTopMargin;
        boolean ifRight = deltaLeftMargin > 0;
        boolean ifDown = deltaTopMargin > 0;
        if (Math.abs(deltaLeftMargin) - mPlanToSort > Math.abs(deltaTopMargin)) {
            if (ifRight) {
                mOriStr += RIGHT;
            } else {
                mOriStr += LEFT;
            }
        } else if (Math.abs(deltaTopMargin) - mPlanToSort > Math.abs(deltaLeftMargin)) {
            if (ifDown) {
                mOriStr += DOWN;
            } else {
                mOriStr += UP;
            }
        }
//        if (mOriStr.length() != 1) {
//            return "";
//        }
        return mOriStr;
    }

    //游戏结束后计算获得的金币
    public static int getCoinsWhenGameOver(int score, int maxCombos, int successfulSorted) {
//        LogAndToastUtil.LogV(score + ": score>>");
        return score / 5 + maxCombos * 2 + successfulSorted;
    }

    //得分 = 1（基础分） + 连击等级（当前连击数除以5）
    public static int getScore(int combos) {
        return 1 + combos / 5;
    }

    //奖励掉落概率计算
    public static int dropItem(int seed){
        Random random = new Random(seed);
        int probability = random.nextInt(100);
        if(95 < probability && probability <= 100 && GameSaveDataSP.getCoinsBonusSwitch()){
            return DROP_COINS;
        }
        if (90 < probability && probability <= 95 && GameSaveDataSP.getTimeBonusSwitch()){
            return DROP_TIME;
        }
        if(85 < probability && probability <= 90 && GameSaveDataSP.getScoreBonusSwitch()){
            return DROP_SCORE;
        }
        return DROP_NOTHING;
    }

    //根据Bonus等级计算奖励参数
    public static int getBonusCoins(int levelCoins){
        return levelCoins * 10;
    }

    public static int getBonusTime(int levelTime){
        return levelTime * 1;
    }

    public static int getBonusScore(int levelScore){
        return levelScore * 10;
    }

    //购买Bonus时需要的金钱
    public static int upgradeBonusCoins(int level){
        switch (level){
            case 0:
                return 1000;
            case 1:
                return 3000;
            case 2:
                return 5000;
            case 3:
                return 10000;
            case 4:
                return 30000;
        }
        return 0;
    }

    //购买Items时需要的金钱
    public static int getItemCoins(String type){
        switch (type){
            case StoreItem.SKIP_CARD_BUY:
                return 300;
            case StoreItem.CHANGE_SORT_BUY:
                return 500;
            case StoreItem.ODD_COMBOS_BUY:
                return 3000;
        }
        return 0;
    }



}
