package com.sorashiro.swipe;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/8
 *
 * 真遗憾，它告诉你游戏结束了
 *
 */

public class GameOverDialog extends Dialog {

    GameOverEvent mGameOverEvent;

    interface GameOverEvent{
        void leaveAndFinish();
    }

    @BindView(R.id.tv_gameover_score_value)
    TextView mTextScore;
    @BindView(R.id.tv_gameover_bonus_score_value)
    TextView mTextBonus;
    @BindView(R.id.tv_gameover_combos_value)
    TextView mTextCombos;
    @BindView(R.id.tv_gameover_sort_value)
    TextView mTextSort;
    @BindView(R.id.tv_gameover_coins_value)
    TextView mTextCoins;

    public GameOverDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public GameOverDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public GameOverDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_game_over);
        ButterKnife.bind(this);
    }

    public void setGameOverEvent(GameOverEvent gameOverEvent){
        this.mGameOverEvent = gameOverEvent;
    }


    public void setResult(String score, String bonus, String combos, String sort, String coins){
        mTextScore.setText(score);
        mTextBonus.setText(bonus);
        mTextCombos.setText(combos);
        mTextSort.setText(sort);
        mTextCoins.setText(coins);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){

        }
    }

    @OnClick(R.id.layout_root)
    public void onClick(View view){
        mGameOverEvent.leaveAndFinish();
        dismiss();
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
