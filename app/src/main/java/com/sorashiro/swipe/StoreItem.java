package com.sorashiro.swipe;

import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author Sora
 * @date 2016/11/8
 *
 * 这一天新建了很多类……
 * 商店界面的布局里，中间是RecyclerView，这些是里面的item
 *
 */

public class StoreItem implements MultiItemEntity{

    public static final int BONUS_TITLE = 1;
    public static final int BONUS_BUY   = 2;
    public static final int ITEMS_TITLE = 3;
    public static final int ITEMS_BUY   = 4;

    public static final String BONUS_COINS_BUY = "bonus_coins_buy";
    public static final String BONUS_TIME_BUY = "bonus_time_buy";
    public static final String BONUS_SCORE_BUY = "bonus_score_buy";

    public static final String SKIP_CARD_BUY = "skip_card_buy";
    public static final String CHANGE_SORT_BUY = "change_sort_buy";
    public static final String ODD_COMBOS_BUY = "odd_combos_buy";

    private int mType;
    //存储显示相应的购买条目名
    private String mName;
    //存储Bonus Level Progress的值或者存储已购买的Item数量
    private int    mNumber;
    //存储购买按钮的类别
    private String mButtonType;

    public StoreItem(int type, String name, int number, String buttonType) {
        mType = type;
        mName = name;
        mNumber = number;
        mButtonType = buttonType;
    }

    public StoreItem(int itemType, String content) {
        this.mType = itemType;
    }

    public StoreItem(int type) {
        this.mType = type;
    }

    public int getItemType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public String getButtonType() {
        return mButtonType;
    }

    public void setButtonType(String buttonType) {
        mButtonType = buttonType;
    }
}
