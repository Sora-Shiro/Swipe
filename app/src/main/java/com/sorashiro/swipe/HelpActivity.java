package com.sorashiro.swipe;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorashiro.swipe.data.GameSaveDataSP;
import com.sorashiro.swipe.utils.LogAndToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * @author Sora
 * @date 2016/11/16
 *
 * 显示帮助信息，或者说，教程信息
 *
 */

public class HelpActivity extends AppCompatActivity{

    @Bind(R.id.vp_help)
    ViewPager mVpHelp;

    ArrayList<View> mViews = new ArrayList<View>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        init();
    }

    //防止后台进入刷新，对部分机型无效
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void init(){

        ButterKnife.bind(this);

        initVp();

    }

    private void initVp(){
        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_help_vp_1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_help_vp_2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.activity_help_vp_3, null);

        mViews.add(view1);
        mViews.add(view2);
        mViews.add(view3);

        mVpHelp.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return mViews.size();
            }
            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));
            }
            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mViews.get(position));
                mViews.get(position).findViewById(R.id.vp_help_exit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkStartOrFinish();
                    }
                });
                return mViews.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

        });
    }

    private void checkStartOrFinish(){
        Intent intent = getIntent();
        boolean isClickHelpBtnResult = intent.getBooleanExtra("isClickHelpBtnResult", false);
        if(!isClickHelpBtnResult){
            Intent intentGame = new Intent(HelpActivity.this, GameActivity.class);
            startActivity(intentGame);
        }
        GameSaveDataSP.init(this);
        GameSaveDataSP.setTutorialOn(false);
        finish();
    }



}
