package com.sorashiro.swipe;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.data.Settings;
import com.sorashiro.swipe.utils.AnimationUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Sora
 * @date 2016/11/7
 *
 * 主菜单，比较简单的功能，唯一酷炫的是会动来动去的logo
 *
 */

public class MainActivity extends AppCompatActivity implements SettingDialog.SettingDialogEvent {

    @Bind(R.id.btn_start)
    Button   mBtnStart;
    @Bind(R.id.tv_title)
    TextView mTextTitle;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAnimatorSet != null && !mAnimatorSet.isRunning()) {
            mAnimatorSet.start();
        }
        playMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAnimatorSet.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    //防止后台进入刷新，对部分机型无效
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void init() {

        ButterKnife.bind(this);

        GameSaveDataSP.init(this);

        initMediaPlayer();

    }

    private boolean mFirstIn = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mFirstIn) {
            initTitleAnim();
            mFirstIn = false;
        }
    }

    //移动效果
    AnimatorSet mAnimatorSet;

    private void initTitleAnim() {
        int go = mTextTitle.getTop();
        ObjectAnimator right = ObjectAnimator
                .ofFloat(mTextTitle, "translationX", 0.0f, go, 0.0f, -go, 0.0f);
        ObjectAnimator up = ObjectAnimator
                .ofFloat(mTextTitle, "translationY", 0.0f, -go, 0.0f, go, 0.0f);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(3000);
        mAnimatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorSet.play(right).before(up);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();

    }

    //更改BGM状态时要调用该函数（然而并没有用上）
    private void toggleMediaPlayer() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    //初始化BGM参数
    private void initMediaPlayer() {
        initMediaPlayerNext();
    }

    private void initMediaPlayerNext() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, Settings.GAME_MUSIC);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public void setMusic(boolean boo) {
        if (boo) {
            playMusic();
        } else {
            stopMusic();
        }
    }

    private void playMusic(){
        if (mMediaPlayer != null && GameSaveDataSP.getMusicOn()) {
            mMediaPlayer.start();
            if (!mMediaPlayer.isPlaying()) {
                initMediaPlayerNext();
                mMediaPlayer.start();
            }
        }
    }

    private void pauseMusic(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void stopMusic(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    //SE还没做出来
    @Override
    public void setSE(boolean boo) {
        if (boo) {

        }
    }


    @OnClick({R.id.btn_start, R.id.btn_store, R.id.btn_setting,
            R.id.btn_rank, R.id.btn_help, R.id.btn_exit})
    public void onBtnClick(View view) {
        AnimationUtil.twinkleButton(view);
        switch (view.getId()) {
            case R.id.btn_start:
                if(GameSaveDataSP.getTutorialOn()){
                    Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
                    intentHelp.putExtra("isClickHelpBtnResult", false);
                    startActivity(intentHelp);
                } else {
                    Intent intentGame = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intentGame);
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                }
                break;
            case R.id.btn_store:
                StoreDialog dialogStore = new StoreDialog(this);
                Window dialogStoreWindow = dialogStore.getWindow();
                assert dialogStoreWindow != null;

                Window dialogWindow = dialogStore.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

                p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
                p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65

                lp.height = p.height;
                lp.width = p.width;
                lp.gravity = Gravity.CENTER;

                dialogWindow.setAttributes(lp);

                dialogStore.show();
                break;
            case R.id.btn_setting:
                SettingDialog dialogSetting = new SettingDialog(this);
                Window dialogSettingWindow = dialogSetting.getWindow();
                assert dialogSettingWindow != null;
                dialogSettingWindow.setGravity(Gravity.CENTER);
                dialogSetting.setSettingDialogEvent(this);
                dialogSetting.show();
                //                GameSaveDataSP.setCoinsLevel(0);
                //                GameSaveDataSP.setCoinsBonusSwitch(false);
                break;
            case R.id.btn_rank:
                RankDialog dialogRank = new RankDialog(this);
                Window dialogRankWindow = dialogRank.getWindow();
                assert dialogRankWindow != null;
                dialogRankWindow.setGravity(Gravity.CENTER);
                dialogRank.show();
                break;
            case R.id.btn_help:
//                int coins = GameSaveDataSP.getCoins();
//                coins += 10000;
//                GameSaveDataSP.setCoins(coins);
                Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
                intentHelp.putExtra("isClickHelpBtnResult", true);
                startActivity(intentHelp);
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}
