package com.sorashiro.swipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorashiro.swipe.data.GameSaveDataSP;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/8
 *
 * 商店系统，买买买，物美价廉 :)
 *
 */

public class StoreDialog extends Dialog implements StoreRVAdapter.StoreRVAdapterEvent{

    @Bind(R.id.rv_store)
    RecyclerView mRvStore;
    @Bind(R.id.tv_coins)
    TextView     mTextCoins;
    @Bind(R.id.img_coins)
    ImageView    mImgCoins;
    @Bind(R.id.btn_leave_to_main)
    Button       mBtnLeaveToMain;

    int mCoins;


    public StoreDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public StoreDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public StoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_store);
        ButterKnife.bind(this);

        GameSaveDataSP.init(getContext());

        initData();
    }

    private void initData(){
        mCoins = GameSaveDataSP.getCoins();
        String coinsText = "" + mCoins;
//        String coinsText = "" + 536870912;
        mTextCoins.setText(coinsText);

        //RecyclerView的数据
        StoreItem itemBonusBuyTitle = new StoreItem(StoreItem.BONUS_TITLE);
        StoreItem itemBonusBuyCoins = new StoreItem(
                StoreItem.BONUS_BUY,
                getContext().getString(R.string.drop_coins_level),
                GameSaveDataSP.getCoinsLevel(),
                StoreItem.BONUS_COINS_BUY);
        StoreItem itemBonusBuyTime = new StoreItem(
                StoreItem.BONUS_BUY,
                getContext().getString(R.string.drop_time_level),
                GameSaveDataSP.getCoinsLevel(),
                StoreItem.BONUS_TIME_BUY);
        StoreItem itemBonusBuyScore = new StoreItem(
                StoreItem.BONUS_BUY,
                getContext().getString(R.string.drop_score_level),
                GameSaveDataSP.getCoinsLevel(),
                StoreItem.BONUS_SCORE_BUY);
        StoreItem itemItemsBuyTitle = new StoreItem(StoreItem.ITEMS_TITLE);
        StoreItem itemItemsBuySkipCard = new StoreItem(
                StoreItem.ITEMS_BUY,
                getContext().getString(R.string.skip_card),
                GameSaveDataSP.getSkipCard(),
                StoreItem.SKIP_CARD_BUY
        );
        StoreItem itemItemsBuyChangeSort = new StoreItem(
                StoreItem.ITEMS_BUY,
                getContext().getString(R.string.change_sort),
                GameSaveDataSP.getSkipCard(),
                StoreItem.CHANGE_SORT_BUY
        );
        StoreItem itemItemsBuyOddCombos = new StoreItem(
                StoreItem.ITEMS_BUY,
                getContext().getString(R.string.odd_combos),
                GameSaveDataSP.getSkipCard(),
                StoreItem.ODD_COMBOS_BUY
        );

        ArrayList<StoreItem> storeItems = new ArrayList<>();
        storeItems.add(itemBonusBuyTitle);
        storeItems.add(itemBonusBuyCoins);
        storeItems.add(itemBonusBuyTime);
        storeItems.add(itemBonusBuyScore);
        storeItems.add(itemItemsBuyTitle);
        storeItems.add(itemItemsBuySkipCard);
        storeItems.add(itemItemsBuyChangeSort);
        storeItems.add(itemItemsBuyOddCombos);


        StoreRVAdapter storeRVAdapter = new StoreRVAdapter(storeItems, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        mRvStore.setLayoutManager(linearLayoutManager);
        mRvStore.setItemAnimator(new DefaultItemAnimator());
        mRvStore.addItemDecoration(new StoreSpacesItemDecoration(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                2,
                getContext().getResources().getColor(R.color.colorUIGreen)));

        mRvStore.setAdapter(storeRVAdapter);
    }

    @OnClick(R.id.btn_leave_to_main)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_leave_to_main:
                break;
        }
        dismiss();
    }

    @Override
    public boolean changeCoins(int coins) {
        String s = "" + coins;
        mTextCoins.setText(s);
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
