package com.sorashiro.swipe.data;

/**
 * @author Sora
 * @date 2016/11/6
 *
 * GameActivity类中用到了这个类，它负责存储当前的卡片类别，元素以及正确的分类方向
 *
 */

public class CardData {

    private String mSortName;
    private String mElement;
    private String mCorrectOri;

    public CardData(String sortName, String element, String correctOri) {
        mSortName = sortName;
        mElement = element;
        mCorrectOri = correctOri;
    }

    public String getSortName() {
        return mSortName;
    }

    public String getElement() {
        return mElement;
    }

    public String getCorrectOri() {
        return mCorrectOri;
    }
}
