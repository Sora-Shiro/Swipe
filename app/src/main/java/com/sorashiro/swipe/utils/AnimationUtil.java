package com.sorashiro.swipe.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sorashiro.swipe.R;

/**
 * @author Sora
 * @date 2016/11/16
 *
 * 我想我是无法忍受重复代码的 :) 但是StoreRVAdapter类，我暂时拿它没辙
 *
 */

public class AnimationUtil {

    public static void twinkleButton(View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.twinkle_btn);
        view.startAnimation(animation);
    }


}
