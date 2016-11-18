package com.sorashiro.swipe;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sorashiro.swipe.data.CardData;
import com.sorashiro.swipe.data.GameSortData;
import com.sorashiro.swipe.data.GameSortDataList;
import com.sorashiro.swipe.utils.AnimationUtil;
import com.sorashiro.swipe.utils.LogAndToastUtil;
import com.sorashiro.swipe.utils.RandomUtil;
import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.data.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Sora
 * @date 2016/11/4
 *
 * 第一个界面(事实上这是GameActivity1的继承者，which created on 11.3)
 *
 * 11.8 方法排序完毕；部分变量出于便于阅读的原因，声明放置在了对应函数的上一行
 *
 * 算法分为多部分，查找" Algorithm x "(x为数字)来快速定位各部分的开头和结尾
 *
 */

public class GameActivity extends AppCompatActivity implements PauseDialog.PauseEvent, GameOverDialog.GameOverEvent {

    private static final String UP    = "U";
    private static final String DOWN  = "D";
    private static final String LEFT  = "L";
    private static final String RIGHT = "R";

    @BindView(R.id.tv_card)
    TextView       mTextCard;
    @BindView(R.id.layout_root)
    RelativeLayout mLayoutRL;
    @BindView(R.id.layout_card)
    RelativeLayout mLayoutCard;
    @BindView(R.id.layout_game)
    RelativeLayout mLayoutGame;
    @BindView(R.id.btn_pause)
    Button         mBtnPause;
    @BindView(R.id.progress_time)
    ProgressBar    mProgressTime;
    @BindView(R.id.tv_up)
    TextView       mTextUp;
    @BindView(R.id.tv_down)
    TextView       mTextDown;
    @BindView(R.id.tv_left)
    TextView       mTextLeft;
    @BindView(R.id.tv_right)
    TextView       mTextRight;
    @BindView(R.id.tv_score)
    TextView       mTextScore;
    @BindView(R.id.tv_sort)
    TextView       mTextSort;
    @BindView(R.id.layout_game_start_and_over)
    RelativeLayout mLayoutGameStartAndOver;
    @BindView(R.id.tv_center)
    TextView       mTextGameStartAndOver;
    @BindView(R.id.img_up)
    ImageView      mImgUp;
    @BindView(R.id.img_down)
    ImageView      mImgDown;
    @BindView(R.id.img_left)
    ImageView      mImgLeft;
    @BindView(R.id.img_right)
    ImageView      mImgRight;
    @BindView(R.id.tv_combos)
    TextView       mTextCombos;
    //奖励拾取
    @BindView(R.id.btn_top_left_corner)
    Button         mBtnTopLeft;
    @BindView(R.id.btn_top_right_corner)
    Button         mBtnTopRight;
    //道具使用
    @BindView(R.id.btn_skip_card)
    Button         mBtnSkipCard;
    @BindView(R.id.btn_change_sort)
    Button         mBtnChangeSort;
    @BindView(R.id.tv_odd_combos)
    TextView       mTextOddCombos;
    //道具数量
    int mSkipCard;
    int mChangeSort;
    int mOddCombos;
    //奖励参数
    int mCoinsLevel;
    int mTimeLevel;
    int mScoreLevel;

    //拖动卡片大于等于此距离会被考虑分类
    private int mPlanToSort;

    //总分
    private int mScore;
    //已分类
    private int mSorted;
    private int mSuccessfulSorted;
    //连击
    private int mCombos;
    private int mMaxCombos;
    //总金币（包括奖励金币）
    private int mCoins;

    //声音处理
    MediaPlayer mMediaPlayer;

    /**
     * Algorithm 1 : 以下部分为游戏初始化操作算法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimePause = false;
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying() && mTimeSubscription != null && GameSaveDataSP.getMusicOn()) {
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimePause = true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    PauseDialog dialog;
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeSubscription != null) {
            mTimeSubscription.unsubscribe();
        }
        if (mBeginSubscription != null) {
            mBeginSubscription.unsubscribe();
        }
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

        //连击提示文字bringToFront
        mTextCombos.bringToFront();

        getWindowMessage();

        initCardsData();

        initItemsAndBonus();

        initStatisticalValue();

        initMediaPlayer();

    }

    //暂时没什么用的变量和函数
    private Display mDisplay;
    private Point   mPoint;

    private void getWindowMessage() {
        mDisplay = this.getWindowManager().getDefaultDisplay();
        mPoint = new Point();
        mDisplay.getSize(mPoint);
    }

    //初始化卡片数据
    private int                     mTotalSorts;
    private ArrayList<GameSortData> mGameSortDataArrayList;
    private GameSortDataList        mGameSortDataList;

    private void initCardsData() {

        mGameSortDataList = new GameSortDataList(this, R.raw.all_sorts_data);
        mGameSortDataArrayList = mGameSortDataList.getGameSortDataArrayList();

        mTotalSorts = mGameSortDataArrayList.size();

        setNextSorts();

        setNextCard();

    }

    //读取已购买功能
    private void initItemsAndBonus() {
        mSkipCard = GameSaveDataSP.getSkipCard();
        mChangeSort = GameSaveDataSP.getChangeSort();
        mOddCombos = GameSaveDataSP.getOddCombos();
        mCoinsLevel = GameSaveDataSP.getCoinsLevel();
        mTimeLevel = GameSaveDataSP.getTimeLevel();
        mScoreLevel = GameSaveDataSP.getScoreLevel();

        //测试用代码
//        mSkipCard = 99;
//        mChangeSort = 99;
//        mOddCombos = 99;
//        mCoinsLevel = 5;
//        mTimeLevel = 5;
//        mScoreLevel = 5;

        String s = getResources().getString(R.string.skip_card) + ": " + mSkipCard;
        mBtnSkipCard.setText(s);
        s = getResources().getString(R.string.change_sort) + ": " + mChangeSort;
        mBtnChangeSort.setText(s);
        s = getResources().getString(R.string.odd_combos) + ": " + mOddCombos;
        mTextOddCombos.setText(s);
    }

    //初始化统计数据，没啥用
    private void initStatisticalValue() {

    }

    //Activity加载完毕后，使mCardLayout置于mGameLayout的中心
    //获取卡片默认位置
    private float mOriginalTopMargin;
    private float mOriginalLeftMargin;
    private boolean mFirstIn = true;
    private Subscription mBeginSubscription;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (mFirstIn) {
                mOriginalTopMargin = mLayoutGame.getHeight() / 2;
                mOriginalLeftMargin = mLayoutGame.getWidth() / 2;
                mOriginalTopMargin -= mLayoutCard.getHeight() / 2;
                mOriginalLeftMargin -= mLayoutCard.getWidth() / 2;
                resetCards(mLayoutCard, (RelativeLayout.LayoutParams) mLayoutCard.getLayoutParams());
                mPlanToSort = (int) mOriginalTopMargin / 3;

                //准备开始
                mBeginSubscription = Observable.interval(1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {
                                //计时开始
                                initProgress();
                                //播放音乐
                                if (mMediaPlayer != null && GameSaveDataSP.getMusicOn()) {
                                    mMediaPlayer.start();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Long integer) {
                                integer = 3 - integer;
                                if (integer > 0) {
                                    String s = "" + integer;
                                    mTextGameStartAndOver.setText(s);
                                } else {
                                    mLayoutGameStartAndOver.setVisibility(View.GONE);
                                    onCompleted();
                                    unsubscribe();
                                }
                            }
                        });

                mFirstIn = false;
            }

        }
    }

    //更改BGM状态时要调用该函数
    private void toggleMediaPlayer() {
        if(!GameSaveDataSP.getMusicOn()){
            return;
        }
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
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, Settings.GAME_MUSIC);
        mMediaPlayer.setLooping(true);

    }

    //倒计时RxJava实现
    private Subscriber<Long> mTimeSubscriber;
    private Subscription     mTimeSubscription;
    private Observable<Long> mTimeObservable;
    private boolean mTimePause     = false;
    //开始时间s
    private int     mBeginTime     = Settings.BEGIN_TIME;
    //当前时间s
    private int     mCurrentTime   = mBeginTime;
    //减少时间的速度s
    private int     mDecreaseSpeed = Settings.DECREASE_BY_SECOND;

    private void initProgress() {
        //1s检查一次状态
        mTimeObservable = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        mTimeSubscriber = new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                if (mTimePause) {
                    return;
                }
                mCurrentTime -= mDecreaseSpeed;
                mProgressTime.setProgress(mCurrentTime * 100 / mBeginTime);
                if (mProgressTime.getProgress() <= 0) {
                    gameOver();
                    mTimeSubscription.unsubscribe();
                }
                dropSomething();
            }
        };

        mTimeSubscription = mTimeObservable.subscribe(mTimeSubscriber);

    }

    /**
     * Algorithm 1 : 以上部分为游戏初始化操作算法
     */

    /**
     * Algorithm 2 : 以下部分为类组和卡片设置及显示操作算法
     */

    //存储游戏中的类别及其元素
    private ArrayList<CardData> mSaveCards = new ArrayList<>();

    private void setNextSorts() {

        //获得4个随机数，得到游戏需要的4个类别
        int[] mSortIndexes = RandomUtil.RandomCommon(0, mTotalSorts, 4);
        assert mSortIndexes != null;

        //显示类别
        mTextUp.setText(mGameSortDataArrayList.get(mSortIndexes[0]).getSortName());
        mTextDown.setText(mGameSortDataArrayList.get(mSortIndexes[1]).getSortName());
        mTextLeft.setText(mGameSortDataArrayList.get(mSortIndexes[2]).getSortName());
        mTextRight.setText(mGameSortDataArrayList.get(mSortIndexes[3]).getSortName());

        //清空当前的mSaveCards，将4个类别的卡片全都加在一起并打乱
        mSaveCards.clear();
        for (int j = 0; j < 4; j++) {
            GameSortData gameSortData = mGameSortDataArrayList.get(mSortIndexes[j]);
            int length = gameSortData.getSortElements().size();
            for (int i = 0; i < length; i++) {
                CardData cardData = new CardData(
                        gameSortData.getSortName(),
                        gameSortData.getSortElements().get(i),
                        getCorrectOri(j)
                );
                mSaveCards.add(cardData);
            }
        }
        Collections.shuffle(mSaveCards);
    }

    //根据是第几组返回对应的答案方向
    private String getCorrectOri(int i) {
        switch (i) {
            case 0:
                return UP;
            case 1:
                return DOWN;
            case 2:
                return LEFT;
            case 3:
                return RIGHT;
        }
        return null;
    }

    //下一卡片文字
    private CardData mNowCard;

    private void setNextCard() {
        //当已分类（不论成功失败）等于20*N种时，类别组刷新
        if (mSorted % 20 == 0) {
            setNextSorts();
        }
        //如果不切换类别，卡片最少要约16*4次的分类才会使mSaveCards空，这取决于被读取的文件
        if (!mSaveCards.isEmpty()) {
            mNowCard = mSaveCards.get(0);
            mTextCard.setText(mNowCard.getElement());
            mSaveCards.remove(0);
        } else {
            setNextSorts();
            setNextCard();
        }
    }

    /**
     * Algorithm 2 : 以上部分为类组和卡片设置及显示操作算法
     */

    /**
     * Algorithm 3 : 以下部分为卡片滑动处理算法，包括
     * 滑动绘制，方向检测，答案校对，校对后的相关参数修改等算法
     */

    //卡片滑动处理
    private float mMoveCardX;
    private float mMoveCardY;

    @OnTouch(R.id.layout_card)
    public boolean onCardsTouch(View view, MotionEvent event) {
        //如果刚好游戏结束，什么都不干
        if(gameOver){
            return true;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mMoveCardX = event.getRawX();
                mMoveCardY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                String s = checkOri(params);
                if (s.length() == 1) {
                    sortCards(view, params, s);
                } else {
                    resetCards(view, params);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                moveViewWithFinger(view, event.getRawX(), event.getRawY());
                break;
        }
        mLayoutRL.invalidate();
        return true;
    }

    //检查卡片运动趋势
    private String checkOri(RelativeLayout.LayoutParams params) {
        return Settings.checkOri(params, mOriginalLeftMargin, mOriginalTopMargin, mPlanToSort);
    }


    //不分组，重新置于中间
    private void resetCards(View view, RelativeLayout.LayoutParams params) {
        params.leftMargin = Float.valueOf(mOriginalLeftMargin).intValue();
        params.topMargin = Float.valueOf(mOriginalTopMargin).intValue();
        view.setLayoutParams(params);
    }

    //进行卡片分组，同时记录最终结果（动画进行过程中可以及时改变选择）
    private void sortCards(final View view, final RelativeLayout.LayoutParams params, final String ori) {
        final int go = params.topMargin * 2 + view.getHeight() * 2;

        //属性动画实现
        ValueAnimator animator = ValueAnimator.ofFloat(0, go);  //定义动画
        final RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) view.getLayoutParams();
        final int topMargin = layoutParams.topMargin;
        final int leftMargin = layoutParams.leftMargin;

        animator.setTarget(view);   //设置作用目标
        animator.setDuration(100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                switch (ori) {
                    case UP:
                        layoutParams.topMargin = (int) (topMargin - value);
                        break;
                    case DOWN:
                        layoutParams.topMargin = (int) (topMargin + value);
                        break;
                    case LEFT:
                        layoutParams.leftMargin = (int) (leftMargin - value);
                        break;
                    case RIGHT:
                        layoutParams.leftMargin = (int) (leftMargin + value);
                        break;
                }
                view.setLayoutParams(layoutParams);
            }

        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //如果游戏结束，卡片动画结束后什么都不发生
                if(gameOver){
                    return;
                }
                resetCards(view, params);
                mSorted += 1;
                if (checkResult(ori)) {
                    answerCorrect();
                } else {
                    answerIncorrect(ori);
                }
                setNextCard();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    //检查移动的方向是否正确
    private boolean checkResult(String result) {
        return result.equals(mNowCard.getCorrectOri());
    }

    int mCorrectAdd = Settings.CORRECT_ADD_TIME;

    private void answerCorrect() {
        //方向区域提示处理
        oriAreaPromptAnim(mNowCard.getCorrectOri(), true);
        //倒计进度条处理
        mCurrentTime += mCorrectAdd;
        mProgressTime.setProgress(mCurrentTime * 100 / mBeginTime);
        //连击处理
        mCombos += 1;
        if (mCombos > mMaxCombos) {
            mMaxCombos = mCombos;
        }
        //连击等于X*N时的处理
        checkCombos();
        //得分处理
        calculateDeservedScore();
        //已成功分类处理
        increaseSuccessfulSorted();
    }

    int mIncorrectDecrease = Settings.INCORRECT_DECREASE_TIME;

    private void answerIncorrect(String ori) {
        //方向区域提示处理
        oriAreaPromptAnim(ori, false);
        oriAreaPromptAnim(mNowCard.getCorrectOri(), true);
        //倒计进度条处理
        mCurrentTime -= mIncorrectDecrease;
        mProgressTime.setProgress(mCurrentTime * 100 / mBeginTime);
        //连击处理
        if (mOddCombos != 0) {
            mOddCombos -= 1;
            GameSaveDataSP.setOddCombos(mOddCombos);
            String s = getResources().getString(R.string.odd_combos) + ": " + mOddCombos;
            mTextOddCombos.setText(s);
        } else {
            mTextCombos.setVisibility(View.GONE);
            if (mCombos >= 5) {
                LogAndToastUtil.ToastOut(this, "连击断了吧啊哈哈哈哈！");
            }
            mCombos = 0;
        }
    }

    private void oriAreaPromptAnim(String ori, boolean ifCorrect) {
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.twinkle);
        //        Animation animation = AnimationUtils.loadAnimation(this, R.anim.twinkle);
        ImageView imageView = null;
        switch (ori) {
            case UP:
                imageView = mImgUp;
                break;
            case DOWN:
                imageView = mImgDown;
                break;
            case LEFT:
                imageView = mImgLeft;
                break;
            case RIGHT:
                imageView = mImgRight;
                break;
        }
        assert imageView != null;
        if (ifCorrect) {
            //尽管是Deprecated的方法，但我要是用新的吧，你又要我升级min API Level，这样我就比较angry
            imageView.setBackgroundColor(getResources().getColor(R.color.colorCorrectGreen));
        } else {
            imageView.setBackgroundColor(getResources().getColor(R.color.colorIncorrectRed));
        }
        final ImageView finalImageView = imageView;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                finalImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finalImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);

    }

    //连击大于等于5的倍数时的处理
    private int mCheckCombos = 5;

    private void checkCombos() {
        if(mCombos >= mCheckCombos){
            String s = "" + mCombos + getResources().getString(R.string.combos);
            mTextCombos.setText(s);
            mTextCombos.setVisibility(View.VISIBLE);
        }
//        if (mCombos % mCheckCombos == 0) {
//            String s = "" + mCombos + getResources().getString(R.string.combos);
//            mTextCombos.setText(s);
//            Animation animation = AnimationUtils.loadAnimation(this,
//                    R.anim.twinkle);
//            final TextView finalTextView = mTextCombos;
//            animation.setAnimationListener(new Animation.AnimationListener() {
//                @Override
//                public void onAnimationStart(Animation animation) {
//                    finalTextView.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    finalTextView.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationRepeat(Animation animation) {
//
//                }
//            });
//            finalTextView.startAnimation(animation);
//        }
    }

    //分数计算公式代入处理
    private void calculateDeservedScore() {
        int score;
        score = Settings.getScore(mCombos);
        mScore += score;
        mTextScore.setText(String.valueOf(mScore));
    }

    //已分类统计处理
    private void increaseSuccessfulSorted() {
        mSuccessfulSorted += 1;
        mTextSort.setText(String.valueOf(mSuccessfulSorted));
    }

    //卡片随手指移动处理
    private void moveViewWithFinger(View view, float nowX, float nowY) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                .getLayoutParams();
        float deltaX = nowX - mMoveCardX;
        float deltaY = nowY - mMoveCardY;
        params.leftMargin = Double.valueOf((deltaX * 1.8 + mOriginalLeftMargin)).intValue();
        params.topMargin = Double.valueOf((deltaY * 1.8 + mOriginalTopMargin)).intValue();
        view.setLayoutParams(params);
    }

    /**
     * Algorithm 3 : 以上部分为卡片滑动处理算法，包括
     * 滑动绘制，方向检测，答案校对，校对后的相关参数修改等算法
     */

    /**
     * Algorithm 4 : 以下为奖励(Bonus)算法
     */
    //存储对应位置的Bonus
    int[] mBonus = new int[4];
    int position;

    private void dropSomething() {
        int dropWhat = Settings.dropItem();
        if (dropWhat == Settings.DROP_NOTHING) {
            //Ahh, pity
            return;
        }
        Button button = null;
        Random random = new Random();
        position = random.nextInt(2);
        Log.v("aaa", position + " : position");
        switch (position) {
            case 0:
                button = mBtnTopLeft;
                break;
            case 1:
                button = mBtnTopRight;
                break;
        }
        assert button != null;
        //如果该位置已经有Bonus则跳出
        if (button.getVisibility() == View.VISIBLE) {
            return;
        }
        AnimationDrawable animationDrawable;
        switch (dropWhat) {
            case Settings.DROP_COINS:
                //No Bought, No Bonus
                if (mCoinsLevel == 0) {
                    return;
                }
                mBonus[position] = Settings.DROP_COINS;
                button.setBackgroundResource(R.drawable.bonus_coins_anim);
                break;
            case Settings.DROP_TIME:
                if (mTimeLevel == 0) {
                    return;
                }
                mBonus[position] = Settings.DROP_TIME;
                button.setBackgroundResource(R.drawable.bonus_time_anim);
                break;
            case Settings.DROP_SCORE:
                if (mScoreLevel == 0) {
                    return;
                }
                mBonus[position] = Settings.DROP_SCORE;
                button.setBackgroundResource(R.drawable.bonus_score_anim);
                break;
        }
        animationDrawable = (AnimationDrawable) button.getBackground();
        animationDrawable.start();
        button.setVisibility(View.VISIBLE);
    }

    /**
     * Algorithm 4 : 以上为奖励算法
     */

    /**
     * Algorithm 5 : 以下为游戏结束后的处理算法
     */

    //游戏结束处理
    boolean gameOver = false;

    private void gameOver() {
        gameOver = true;

        //随机获得基于当前分数的奖励分数
        Random random = new Random();
        int bonusScore = random.nextInt(mScore);
        int saveScore = mScore;
        mScore += bonusScore;

        //计算应得金币
        int getCoins = Settings.getCoinsWhenGameOver(mScore, mMaxCombos, mSuccessfulSorted);
        mCoins += getCoins;
        int coinsTotal = GameSaveDataSP.getCoins();
        //检查总金币是否超过上限
        if(coinsTotal + mCoins >= Settings.LIMIT_COINS){
            coinsTotal = Settings.LIMIT_COINS;
        } else {
            coinsTotal += mCoins;
        }
        //修改玩家金币数
        GameSaveDataSP.setCoins(coinsTotal);

        String scoreStr = saveScore + "";
        String bonusStr = bonusScore + "";
        String combosStr = mMaxCombos + "";
        String sortStr = mSuccessfulSorted + "";
        String coinsStr = mCoins + "";
        GameOverDialog dialogGameOver = new GameOverDialog(this);
        Window dialogStoreWindow = dialogGameOver.getWindow();
        assert dialogStoreWindow != null;
        dialogStoreWindow.setGravity(Gravity.CENTER);
        dialogGameOver.show();
        dialogGameOver.setResult(scoreStr, bonusStr, combosStr, sortStr, coinsStr);
        dialogGameOver.setGameOverEvent(this);

        checkBreakHistory(mCoins);

    }

    @Override
    public void leaveAndFinish() {
        finish();
    }

    private String checkBreakHistory(int getCoins) {
        String s = "";
        int maxScore = GameSaveDataSP.getMaxScore();
        int maxCombos = GameSaveDataSP.getMaxCombos();
        int maxSuccessfulSort = GameSaveDataSP.getMaxSuccessfulSort();
        int maxCoins = GameSaveDataSP.getMaxCoins();
        //str.indexOf("23") != -1
        if (mScore > maxScore) {
            GameSaveDataSP.setMaxScore(mScore);
            s += "score";
        }
        if (mMaxCombos > maxCombos) {
            GameSaveDataSP.setMaxCombos(mMaxCombos);
            s += "combo";
        }
        if (mSuccessfulSorted > maxSuccessfulSort) {
            GameSaveDataSP.setMaxSuccessfulSort(mSuccessfulSorted);
            s += "sort";
        }
        if (getCoins > maxCoins) {
            GameSaveDataSP.setMaxCoins(getCoins);
            s += "coin";
        }
        return s;
    }

    /**
     * Algorithm 5 : 以上为游戏结束后的处理算法
     */

    /**
     * Algorithm 6 : 以下为游戏界面各个可响应Click时间的控件的处理算法
     */

    //跳过卡片道具使用
    @OnClick(R.id.btn_skip_card)
    public void onBtnSkipCardClick(View view) {
        AnimationUtil.twinkleButton(view);
        if (mSkipCard != 0) {
            mSorted += 1;
            setNextCard();
            mSkipCard -= 1;
            GameSaveDataSP.setSkipCard(mSkipCard);
            String s = getResources().getString(R.string.skip_card) + ": " + mSkipCard;
            mBtnSkipCard.setText(s);
        } else {
            LogAndToastUtil.ToastOut(this, "别点了！没有了！");
        }
    }

    //重选分类道具使用
    @OnClick(R.id.btn_change_sort)
    public void onBtnChangeSortClick(View view) {
        AnimationUtil.twinkleButton(view);
        if (mChangeSort != 0) {
            setNextSorts();
            setNextCard();
            mChangeSort -= 1;
            GameSaveDataSP.setChangeSort(mChangeSort);
            String s = getResources().getString(R.string.change_sort) + ": " + mChangeSort;
            mBtnChangeSort.setText(s);
        } else {
            LogAndToastUtil.ToastOut(this, "别点了！没有了！");
        }
    }

    @OnClick({R.id.btn_top_left_corner, R.id.btn_top_right_corner})
    public void onBtnCornerClick(View view) {
        int bonus = 0;
        //可以考虑把bonus设为继承了Button类的控件的属性，但可能不太灵活
        switch (view.getId()) {
            case R.id.btn_top_left_corner:
                bonus = mBonus[0];
                break;
            case R.id.btn_top_right_corner:
                bonus = mBonus[1];
                break;
        }
        switch (bonus) {
            case Settings.DROP_COINS:
                mCoins += Settings.getBonusCoins(mCoinsLevel);
                break;
            case Settings.DROP_TIME:
                mCurrentTime += Settings.getBonusTime(mTimeLevel);
                mProgressTime.setProgress(mCurrentTime * 100 / mBeginTime);
                break;
            case Settings.DROP_SCORE:
                mScore += Settings.getBonusScore(mScoreLevel);
                String s = "" + mScore;
                mTextScore.setText(s);
                break;
        }
        view.setVisibility(View.GONE);
    }


    //pause_btn
    @OnClick(R.id.btn_pause)
    public void onBtnPauseClick(View view) {
        mTimePause = !mTimePause;
        AnimationUtil.twinkleButton(view);
        PauseDialog dialog = new PauseDialog(this);
        Window dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setPauseEvent(this);

        dialog.show();

        toggleMediaPlayer();
    }

    @Override
    public void resume() {
        mTimePause = !mTimePause;
        toggleMediaPlayer();
    }

    @Override
    public void backToMain() {
        finish();
    }


    //游戏结束后点击界面finish
    @OnClick(R.id.layout_game_start_and_over)
    public void onLayoutGameStartAndOverClick() {
        if (gameOver) {
            finish();
        }
    }



    /**
     * Algorithm 6 : 以上为游戏界面各个可响应Click时间的控件的处理算法
     */
}
