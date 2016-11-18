package com.sorashiro.swipe;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.utils.AnimationUtil;
import com.sorashiro.swipe.utils.LogAndToastUtil;
import com.sorashiro.swipe.data.Settings;

import java.util.List;


/**
 * @author Sora
 * @date 2016/11/8
 *
 * 这是Adapter，没什么好说的
 * 看起来似乎有些长，但其实就是一些购买处理
 *
 */

public class StoreRVAdapter extends BaseMultiItemQuickAdapter<StoreItem, BaseViewHolder> {

    private Context             mContext;
    private StoreRVAdapterEvent mStoreRVAdapterEvent;

    interface StoreRVAdapterEvent {
        boolean changeCoins(int coins);
    }

    public StoreRVAdapter(List<StoreItem> data, Dialog dialog) {
        super(data);
        this.mContext = dialog.getContext();
        this.mStoreRVAdapterEvent = (StoreRVAdapterEvent) dialog;
        GameSaveDataSP.init(mContext);
        addItemType(StoreItem.BONUS_TITLE, R.layout.dialog_store_bonus_title);
        addItemType(StoreItem.BONUS_BUY, R.layout.dialog_store_bonus_buy);
        addItemType(StoreItem.ITEMS_TITLE, R.layout.dialog_store_items_title);
        addItemType(StoreItem.ITEMS_BUY, R.layout.dialog_store_items_buy);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final StoreItem item) {
        TextView textTitle;
        Button buyButton;
        TextView textNeed;
        TextView textItemsNum;
        final int position = helper.getLayoutPosition();
        switch (item.getItemType()) {
            case StoreItem.BONUS_TITLE:
                //                textView = helper.getView(R.id.tv_rv_title);
                //                textView.setText(item.getName());
                break;
            case StoreItem.BONUS_BUY:
                textTitle = helper.getView(R.id.tv_rv_title);
                textTitle.setText(item.getName());

                ProgressBar progressBar = helper.getView(R.id.progress_rv_level);
                progressBar.setMax(Settings.MAX_LEVEL);

                buyButton = helper.getView(R.id.btn_rv_buy);
                textNeed = helper.getView(R.id.tv_need_coins);
                //唯一标识符
                buyButton.setTag(item.getButtonType());
                initLayoutBonusBuy(buyButton, textNeed, progressBar, item.getButtonType());
                //                item.setTextNeedCoins(textNeed);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        StoreItem storeItem = getItem(position);
                        //                        String needCoins = storeItem.getNeedCoins() + "";
                        AnimationUtil.twinkleButton(v);
                        TextView textViewNeedCoins = helper.getView(R.id.tv_need_coins);
                        ProgressBar progressLevel = helper.getView(R.id.progress_rv_level);
                        int coins = GameSaveDataSP.getCoins();
                        switch ((String) v.getTag()) {
                            case StoreItem.BONUS_COINS_BUY:
                                coins = buyBonus(StoreItem.BONUS_COINS_BUY, coins);
                                break;
                            case StoreItem.BONUS_TIME_BUY:
                                coins = buyBonus(StoreItem.BONUS_TIME_BUY, coins);
                                break;
                            case StoreItem.BONUS_SCORE_BUY:
                                coins = buyBonus(StoreItem.BONUS_SCORE_BUY, coins);
                                break;
                        }
                        initLayoutBonusBuy((Button) v, textViewNeedCoins, progressLevel, (String) v.getTag());
                        mStoreRVAdapterEvent.changeCoins(coins);
                    }
                });
                break;
            case StoreItem.ITEMS_TITLE:
                //                textView = helper.getView(R.id.tv_rv_title);
                //                textView.setText(item.getName());
                break;
            case StoreItem.ITEMS_BUY:
                textTitle = helper.getView(R.id.tv_rv_title);
                textTitle.setText(item.getName());

                buyButton = helper.getView(R.id.btn_rv_buy);
                textItemsNum = helper.getView(R.id.tv_items_number);
                //唯一标识符
                buyButton.setTag(item.getButtonType());
                initLayoutItemsBuy(buyButton, textItemsNum, item.getButtonType());
                //                item.setTextNeedCoins(textNeed);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //                        StoreItem storeItem = getItem(position);
                        //                        String needCoins = storeItem.getNeedCoins() + "";
                        AnimationUtil.twinkleButton(v);
                        TextView textItemsNum = helper.getView(R.id.tv_items_number);
                        int coins = GameSaveDataSP.getCoins();
                        switch ((String) v.getTag()) {
                            case StoreItem.SKIP_CARD_BUY:
                                coins = buyItems(StoreItem.SKIP_CARD_BUY, coins);
                                break;
                            case StoreItem.CHANGE_SORT_BUY:
                                coins = buyItems(StoreItem.CHANGE_SORT_BUY, coins);
                                break;
                            case StoreItem.ODD_COMBOS_BUY:
                                coins = buyItems(StoreItem.ODD_COMBOS_BUY, coins);
                                break;
                        }
                        initLayoutItemsBuy((Button) v, textItemsNum, (String) v.getTag());
                        mStoreRVAdapterEvent.changeCoins(coins);
                    }
                });
                break;
        }
    }

    private void initLayoutBonusBuy(Button button, TextView textView, ProgressBar progressBar, String type) {
        int level = 0;
        boolean switchOn = false;
        switch (type) {
            case StoreItem.BONUS_COINS_BUY:
                level = GameSaveDataSP.getCoinsLevel();
                switchOn = GameSaveDataSP.getCoinsBonusSwitch();
                break;
            case StoreItem.BONUS_TIME_BUY:
                level = GameSaveDataSP.getTimeLevel();
                switchOn = GameSaveDataSP.getTimeBonusSwitch();
                break;
            case StoreItem.BONUS_SCORE_BUY:
                level = GameSaveDataSP.getScoreLevel();
                switchOn = GameSaveDataSP.getScoreBonusSwitch();
                break;
        }
        String s;
        //TextView处理
        if (level == 0) {
            button.setText(mContext.getResources().getString(R.string.unlock));
            s = mContext.getResources().getString(R.string.next_level_need_coins) +
                    Settings.upgradeBonusCoins(level);
            textView.setText(s);
            textView.setVisibility(View.VISIBLE);
        } else if (level == Settings.MAX_LEVEL) {
            if (switchOn) {
                button.setText(mContext.getResources().getString(R.string.close));
            } else {
                button.setText(mContext.getResources().getString(R.string.open));
            }
            textView.setVisibility(View.INVISIBLE);
        } else {
            button.setText(mContext.getResources().getString(R.string.upgrade));
            s = mContext.getResources().getString(R.string.next_level_need_coins) +
                    Settings.upgradeBonusCoins(level);
            textView.setText(s);
            textView.setVisibility(View.VISIBLE);
        }
        //ProgressBar处理
        progressBar.setProgress(level);
    }

    private void initLayoutItemsBuy(Button button, TextView textItemsNum, String type) {
        int num = 0;
        int needCoins = 0;
        switch (type) {
            case StoreItem.SKIP_CARD_BUY:
                num = GameSaveDataSP.getSkipCard();
                needCoins = Settings.getItemCoins(StoreItem.SKIP_CARD_BUY);
                break;
            case StoreItem.CHANGE_SORT_BUY:
                num = GameSaveDataSP.getChangeSort();
                needCoins = Settings.getItemCoins(StoreItem.CHANGE_SORT_BUY);
                break;
            case StoreItem.ODD_COMBOS_BUY:
                num = GameSaveDataSP.getOddCombos();
                needCoins = Settings.getItemCoins(StoreItem.ODD_COMBOS_BUY);
                break;
        }
        String s;
        //Button处理
        if (num < Settings.MAX_ITEM_NUM) {
            Resources resources = mContext.getResources();
            s = resources.getString(R.string.spend)
                    + " " + needCoins + " "
                    + resources.getString(R.string.coins_buy);
            button.setText(s);
            button.setClickable(true);
        } else {
            button.setText(mContext.getResources().getString(R.string.you_cannot_buy_any_more));
            button.setClickable(false);
        }
        //已拥有数量显示
        String numStr = "" + num;
        textItemsNum.setText(numStr);
    }


    public int buyBonus(String thing, int mCoins) {
        int level = 0;
        boolean switchOn = false;
        int need = 0;
        switch (thing) {
            case StoreItem.BONUS_COINS_BUY:
                level = GameSaveDataSP.getCoinsLevel();
                switchOn = GameSaveDataSP.getCoinsBonusSwitch();
                need = Settings.upgradeBonusCoins(level);
                break;
            case StoreItem.BONUS_TIME_BUY:
                level = GameSaveDataSP.getTimeLevel();
                switchOn = GameSaveDataSP.getTimeBonusSwitch();
                need = Settings.upgradeBonusCoins(level);
                break;
            case StoreItem.BONUS_SCORE_BUY:
                level = GameSaveDataSP.getScoreLevel();
                switchOn = GameSaveDataSP.getScoreBonusSwitch();
                need = Settings.upgradeBonusCoins(level);
                break;
        }
        if (level < Settings.MAX_LEVEL) {
            if (mCoins >= need) {
                mCoins -= need;
                GameSaveDataSP.setCoins(mCoins);
            } else {
                LogAndToastUtil.ToastOut(mContext, "没钱了！买不到了！");
                return mCoins;
            }
            level += 1;
            switch (thing) {
                case StoreItem.BONUS_COINS_BUY:
                    GameSaveDataSP.setCoinsLevel(level);
                    GameSaveDataSP.setCoinsBonusSwitch(true);
                    break;
                case StoreItem.BONUS_TIME_BUY:
                    GameSaveDataSP.setTimeLevel(level);
                    GameSaveDataSP.setTimeBonusSwitch(true);
                    break;
                case StoreItem.BONUS_SCORE_BUY:
                    GameSaveDataSP.setScoreLevel(level);
                    GameSaveDataSP.setScoreBonusSwitch(true);
                    break;
            }
        } else {
            switchOn = !switchOn;
            switch (thing) {
                case StoreItem.BONUS_COINS_BUY:
                    GameSaveDataSP.setCoinsBonusSwitch(switchOn);
                    break;
                case StoreItem.BONUS_TIME_BUY:
                    GameSaveDataSP.setTimeBonusSwitch(switchOn);
                    break;
                case StoreItem.BONUS_SCORE_BUY:
                    GameSaveDataSP.setScoreBonusSwitch(switchOn);
                    break;
            }
        }
        return mCoins;
    }

    public int buyItems(String thing, int mCoins) {
        int num = 0;
        int needCoins = 0;
        switch (thing) {
            case StoreItem.SKIP_CARD_BUY:
                num = GameSaveDataSP.getSkipCard();
                needCoins = Settings.getItemCoins(StoreItem.SKIP_CARD_BUY);
                break;
            case StoreItem.CHANGE_SORT_BUY:
                num = GameSaveDataSP.getChangeSort();
                needCoins = Settings.getItemCoins(StoreItem.CHANGE_SORT_BUY);
                break;
            case StoreItem.ODD_COMBOS_BUY:
                num = GameSaveDataSP.getOddCombos();
                needCoins = Settings.getItemCoins(StoreItem.ODD_COMBOS_BUY);
                break;
        }
        if (num <= Settings.MAX_ITEM_NUM) {
            if (mCoins >= needCoins) {
                mCoins -= needCoins;
                GameSaveDataSP.setCoins(mCoins);
            } else {
                LogAndToastUtil.ToastOut(mContext, "没钱了！买不到了！");
                return mCoins;
            }
            num += 1;
            switch (thing) {
                case StoreItem.SKIP_CARD_BUY:
                    GameSaveDataSP.setSkipCard(num);
                    break;
                case StoreItem.CHANGE_SORT_BUY:
                    GameSaveDataSP.setChangeSort(num);
                    break;
                case StoreItem.ODD_COMBOS_BUY:
                    GameSaveDataSP.setOddCombos(num);
                    break;
            }
        } else {
            //事实上这里不会被执行，因为button的clickable是false
            LogAndToastUtil.ToastOut(mContext, "别买了！你买得够多了！");
        }
        return mCoins;
    }

}