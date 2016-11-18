package com.sorashiro.swipe.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Sora
 * @date 2016/11/7
 *
 * 玩家的数据都在这里了！
 * 修改数据前务必先调用init()方法！
 *
 */

public class GameSaveDataSP {

    //主目录
    private static final String ITEMS_AND_BONUS    = "items_and_bonus";

    //相关道具数量与Bonus参数
    private static final String SKIP_CARD          = "skip_card";
    private static final String CHANGE_SORT        = "change_sort";
    private static final String ODD_COMBOS         = "odd_combos";

    private static final String COINS_LEVEL        = "coins_level";
    private static final String TIME_LEVEL         = "time_level";
    private static final String SCORE_LEVEL        = "score_level";

    private static final String COINS_BONUS_SWITCH = "coins_bonus_switch";
    private static final String TIME_BONUS_SWITCH  = "time_bonus_switch";
    private static final String SCORE_BONUS_SWITCH = "score_bonus_switch";

    //持有金币
    private static final String COINS = "coins";

    //榜单记录
    private static final String MAX_SCORE = "max_score";
    private static final String MAX_COMBOS = "max_combos";
    private static final String MAX_SUCCESSFUL_SORT = "max_successful_sort";
    private static final String MAX_COINS = "max_coins";

    //游戏设置
    private static final String MUSIC = "music";
    private static final String SE = "se";
    private static final String TUTORIAL = "tutorial";


    private static SharedPreferences        sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    private GameSaveDataSP() {
    }

    //修改或获得数据前必须先调用该函数
    public static void init(Context context) {
        sSharedPreferences = context.getSharedPreferences(ITEMS_AND_BONUS, Context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
        sEditor.commit();
    }

    public static int getCoins() {
        return sSharedPreferences.getInt(COINS, 3000);
    }

    public static void setCoins(int coins) {
        sEditor.putInt(COINS, coins).commit();
    }

    public static int getSkipCard() {
        return sSharedPreferences.getInt(SKIP_CARD, 3);
    }

    public static void setSkipCard(int skipCard) {
        sEditor.putInt(SKIP_CARD, skipCard).commit();
    }

    public static int getChangeSort() {
        return sSharedPreferences.getInt(CHANGE_SORT, 3);
    }

    public static void setChangeSort(int changeSort) {
        sEditor.putInt(CHANGE_SORT, changeSort).commit();
    }

    public static int getOddCombos() {
        return sSharedPreferences.getInt(ODD_COMBOS, 0);
    }

    public static void setOddCombos(int oddCombos) {
        sEditor.putInt(ODD_COMBOS, oddCombos).commit();
    }

    public static int getCoinsLevel() {
        //        CoinsLevel = sSharedPreferences.getInt(COINS_LEVEL, 0);
        return sSharedPreferences.getInt(COINS_LEVEL, 0);
    }

    public static void setCoinsLevel(int coinsLevel) {
        sEditor.putInt(COINS_LEVEL, coinsLevel).commit();
    }

    public static int getTimeLevel() {
        return sSharedPreferences.getInt(TIME_LEVEL, 0);
    }

    public static void setTimeLevel(int timeLevel) {
        sEditor.putInt(TIME_LEVEL, timeLevel).commit();
    }

    public static int getScoreLevel() {
        return sSharedPreferences.getInt(SCORE_LEVEL, 0);
    }

    public static void setScoreLevel(int scoreLevel) {
        sEditor.putInt(SCORE_LEVEL, scoreLevel).commit();
    }

    public static boolean getCoinsBonusSwitch() {
        return sSharedPreferences.getBoolean(COINS_BONUS_SWITCH, true);
    }

    public static void setCoinsBonusSwitch(boolean boo) {
        sEditor.putBoolean(COINS_BONUS_SWITCH, boo).commit();
    }

    public static boolean getTimeBonusSwitch() {
        return sSharedPreferences.getBoolean(TIME_BONUS_SWITCH, true);
    }

    public static void setTimeBonusSwitch(boolean boo) {
        sEditor.putBoolean(TIME_BONUS_SWITCH, boo).commit();
    }

    public static boolean getScoreBonusSwitch() {
        return sSharedPreferences.getBoolean(SCORE_BONUS_SWITCH, true);
    }

    public static void setScoreBonusSwitch(boolean boo) {
        sEditor.putBoolean(SCORE_BONUS_SWITCH, boo).commit();
    }

    public static int getMaxScore() {
        return sSharedPreferences.getInt(MAX_SCORE, 0);
    }

    public static void setMaxScore(int max) {
        sEditor.putInt(MAX_SCORE, max).commit();
    }

    public static int getMaxCombos() {
        return sSharedPreferences.getInt(MAX_COMBOS, 0);
    }

    public static void setMaxCombos(int max) {
        sEditor.putInt(MAX_COMBOS, max).commit();
    }

    public static int getMaxSuccessfulSort() {
        return sSharedPreferences.getInt(MAX_SUCCESSFUL_SORT, 0);
    }

    public static void setMaxSuccessfulSort(int max) {
        sEditor.putInt(MAX_SUCCESSFUL_SORT, max).commit();
    }

    public static int getMaxCoins() {
        return sSharedPreferences.getInt(MAX_COINS, 0);
    }

    public static void setMaxCoins(int max) {
        sEditor.putInt(MAX_COINS, max).commit();
    }

    public static boolean getMusicOn() {
        return sSharedPreferences.getBoolean(MUSIC, true);
    }

    public static void setMusicOn(boolean boo) {
        sEditor.putBoolean(MUSIC, boo).commit();
    }

    public static boolean getSEOn() {
        return sSharedPreferences.getBoolean(SE, true);
    }

    public static void setSEOn(boolean boo) {
        sEditor.putBoolean(SE, boo).commit();
    }

    public static boolean getTutorialOn() {
        return sSharedPreferences.getBoolean(TUTORIAL, true);
    }

    public static void setTutorialOn(boolean boo) {
        sEditor.putBoolean(TUTORIAL, boo).commit();
    }

}
