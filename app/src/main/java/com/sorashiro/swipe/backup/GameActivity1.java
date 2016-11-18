package com.sorashiro.swipe.backup;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sorashiro.swipe.R;
import com.sorashiro.swipe.utils.LogAndToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * @author Sora
 * @date 2016/11/4
 *
 * 对于这个项目来说，这里是一切的开始，核心算法的开始
 * 但是现在来看，这些可能并不能直接运行 :)
 *
 */

public class GameActivity1 extends AppCompatActivity {

    @Bind(R.id.tv_card)
    TextView mCards;
    @Bind(R.id.layout_root)
    RelativeLayout layoutRL;

    //拖动卡片大于等于此距离会被考虑分类
    int mPlanToSort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();
    }

    private void init() {

        ButterKnife.bind(this);

        getWindowMessage();

        initCards();
    }

    Display mDisplay;
    Point   mPoint;
    private void getWindowMessage() {
        mDisplay = this.getWindowManager().getDefaultDisplay();
        mPoint = new Point();
        mDisplay.getSize(mPoint);

        mPlanToSort = mPoint.x / 6;
        LogAndToastUtil.LogV("mPlanToSort: " + mPlanToSort);
        LogAndToastUtil.LogV("mPoint.x :" + mPoint.x + "\nmPoint.y: " + mPoint.y);

    }

    //初始化卡片数据
    private void initCards() {

    }

    //卡片滑动处理
    float x;
    float y;
    float save_top_margin  = 0;
    float save_left_margin = 0;
    @OnTouch(R.id.tv_card)
    public boolean onCardsTouch(View view, MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = event.getRawX();
                y = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                save_top_margin = params.topMargin;
                save_left_margin = params.leftMargin;
                LogAndToastUtil.LogV(view.getY() + "  view.getY()");
                LogAndToastUtil.LogV(view.getX() + "  view.getX()");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                moveViewWithFinger(view, event.getRawX(), event.getRawY());
                break;
        }
        layoutRL.invalidate();
        return true;
    }

    private void moveViewWithFinger(View view, float nowX, float nowY) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                .getLayoutParams();
        float deltaX = nowX - x;
        float deltaY = nowY - y;
        params.leftMargin = Double.valueOf((deltaX*1.8 + save_left_margin)).intValue();
        params.topMargin = Double.valueOf((deltaY*1.8 + save_top_margin)).intValue();
        view.setLayoutParams(params);
    }

}
