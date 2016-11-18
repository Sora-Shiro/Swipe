package com.sorashiro.swipe;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;
import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.utils.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/9
 *
 * 设置，音乐开关，音效开关，教学开关，以及该app的介绍入口按钮
 *
 */

public class SettingDialog extends Dialog {

    interface SettingDialogEvent{
        void setMusic(boolean boo);
        void setSE(boolean boo);
    }

    private SettingDialogEvent mSettingDialogEvent;

    @BindView(R.id.sb_music)
    SwitchButton mSbMusic;
    @BindView(R.id.sb_se)
    SwitchButton mSbSE;
    @BindView(R.id.sb_tutorial)
    SwitchButton mSbTutorial;
    @BindView(R.id.btn_about)
    Button       mBtnAbout;
    @BindView(R.id.btn_leave_to_main)
    Button       mLeaveToMain;

    public SettingDialog(Context context) {
        super(context, R.style.add_dialog);
    }

    public SettingDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public SettingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        ButterKnife.bind(this);

        init();
    }

    private boolean ifFirst = true;
    private void init() {
        boolean music = GameSaveDataSP.getMusicOn();
        boolean se = GameSaveDataSP.getSEOn();
        boolean tutorial = GameSaveDataSP.getTutorialOn();
        mSbMusic.setChecked(music);
        mSbSE.setChecked(se);
        mSbTutorial.setChecked(tutorial);
        mSettingDialogEvent.setMusic(music);
        mSettingDialogEvent.setSE(se);
        if(ifFirst){
            initAnim();
            ifFirst = false;
        }
    }

    private void initAnim(){
        ObjectAnimator alphaAnimSbMusic = ObjectAnimator
                .ofFloat(mSbMusic, "alpha", 0.0f, 1.0f);
        ObjectAnimator alphaAnimSbSE = ObjectAnimator
                .ofFloat(mSbSE, "alpha", 0.0f, 1.0f);
        ObjectAnimator alphaAnimSbTutorial = ObjectAnimator
                .ofFloat(mSbTutorial, "alpha", 0.0f, 1.0f);
        alphaAnimSbMusic.setDuration(1000);
        alphaAnimSbSE.setDuration(1000);
        alphaAnimSbTutorial.setDuration(1000);
        alphaAnimSbMusic.setInterpolator(new LinearInterpolator());
        alphaAnimSbSE.setInterpolator(new LinearInterpolator());
        alphaAnimSbTutorial.setInterpolator(new LinearInterpolator());
        alphaAnimSbMusic.start();
        alphaAnimSbSE.start();
        alphaAnimSbTutorial.start();
    }

    public void setSettingDialogEvent(SettingDialogEvent settingDialogEvent){
        this.mSettingDialogEvent = settingDialogEvent;
    }

    @OnCheckedChanged({R.id.sb_music, R.id.sb_se, R.id.sb_tutorial})
    public void onCheckChanged(SwitchButton button, boolean isChecked) {
        switch (button.getId()) {
            case R.id.sb_music:
                GameSaveDataSP.setMusicOn(isChecked);
                break;
            case R.id.sb_se:
                GameSaveDataSP.setSEOn(isChecked);
                break;
            case R.id.sb_tutorial:
                GameSaveDataSP.setTutorialOn(isChecked);
                break;
        }
        init();
    }

    @OnClick({R.id.btn_about, R.id.btn_leave_to_main})
    public void onClick(View view) {
        AnimationUtil.twinkleButton(view);
        switch (view.getId()) {
            case R.id.btn_about:
                Intent intent = new Intent(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.btn_leave_to_main:
                dismiss();
                break;
        }
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
