package com.sorashiro.swipe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.sorashiro.swipe.utils.AnimationUtil;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/8
 *
 * 趁着暂停对话框出现的时候，你可以作弊，思考一下该怎么分类
 * 但是我还是不推荐玩家这么做 :) 或许之后我会设定一个惩罚机制
 *
 */

public class PauseDialog extends Dialog {

    @BindView(R.id.btn_resume)
    Button mBtnResume;
    @BindView(R.id.btn_back_to_main)
    Button mBackToMain;

    private PauseEvent mPauseEvent;
    private Context mContext;

    interface PauseEvent{
        void resume();
        void backToMain();
    }

    public PauseDialog(Context context) {
        super(context, R.style.add_dialog);
        this.mContext = context;
    }

    public PauseDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public PauseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pause);
        ButterKnife.bind(this);
    }

    public void setPauseEvent(PauseEvent p){
        this.mPauseEvent = p;
    }

    @OnClick({R.id.btn_resume, R.id.btn_back_to_main})
    public void onClick(View view){
        AnimationUtil.twinkleButton(view);
        switch (view.getId()){
            case R.id.btn_resume:
                mPauseEvent.resume();
                break;
            case R.id.btn_back_to_main:
                mPauseEvent.backToMain();
                break;
        }
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
