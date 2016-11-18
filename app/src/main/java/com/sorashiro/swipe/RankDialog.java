package com.sorashiro.swipe;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.utils.AnimationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/10
 *
 * 历史最高记录，记录了玩家的最高分、最高连击、最高成功分类
 * 后续可能会加上最高金币
 *
 */

public class RankDialog extends Dialog {

    @Bind(R.id.tv_max_score_value)
    TextView mTextScore;
    @Bind(R.id.tv_max_combos_value)
    TextView mTextCombos;
    @Bind(R.id.tv_max_successful_sort_value)
    TextView mTextSort;

    public RankDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public RankDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public RankDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rank);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        String score = GameSaveDataSP.getMaxScore() + "";
        String combos = GameSaveDataSP.getMaxCombos() + "";
        String sort = GameSaveDataSP.getMaxSuccessfulSort() + "";
        mTextScore.setText(score);
        mTextCombos.setText(combos);
        mTextSort.setText(sort);
    }

    @OnClick({R.id.btn_leave_to_main})
    public void onClick(View view){
        AnimationUtil.twinkleButton(view);
        switch (view.getId()){
            case R.id.btn_back_to_main:
                break;
        }
        dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
