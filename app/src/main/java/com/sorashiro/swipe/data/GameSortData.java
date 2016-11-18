package com.sorashiro.swipe.data;

import java.util.ArrayList;

/**
 * @author Sora
 * @date 2016/11/5
 *
 * 卡片数据，配合它的List使用
 *
 */

public class GameSortData {

    private String            mSortName;
    private ArrayList<String> mSortElements;

    public GameSortData(String s) {
        this.mSortName = s;
        mSortElements = new ArrayList<>();
    }

    public String getSortName() {
        return mSortName;
    }

    public ArrayList<String> getSortElements() {
        return mSortElements;
    }
}
