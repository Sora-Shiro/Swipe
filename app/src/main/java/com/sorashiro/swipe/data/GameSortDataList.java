package com.sorashiro.swipe.data;

import android.content.Context;

import com.sorashiro.swipe.utils.LogAndToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Sora
 * @date 2016/11/5
 *
 * 这个类负责读取本地核心数据文件并保存一下
 *
 */

public class GameSortDataList {
    private Context                 mContext;
    private ArrayList<GameSortData> mGameSortDataArrayList;
    private GameSortData            mGameSortData;

    private InputStream       is;
    private InputStreamReader isr;
    private BufferedReader    br;

    public GameSortDataList(Context context, int file) {
        this.mContext = context;
        this.mGameSortDataArrayList = new ArrayList<>();
        openStream(file);
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openStream(int file) {
        is = mContext.getResources().openRawResource(file);
        try {
            isr = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            LogAndToastUtil.ToastOut(mContext, "无法打开数据文件，请尝试重启游戏");
        }
        br = new BufferedReader(isr);
    }

    public void readFile() throws IOException {
        String line;
        //不断读取直到文件被读取处理完为止
        while(true) {
            line = br.readLine();
            if (line != null) {
                readingFile(line);
            } else {
                closeStream();
//                LogAndToastUtil.ToastOut(mContext, "游戏数据已经读取完毕");
                break;
            }
        }
    }

    private void closeStream() throws IOException {
        br.close();
        isr.close();
        is.close();
    }

    //在文本里读取字符并进行处理，可能需要重构
    //记得以非BOM格式的UTF-8编码txt！
    private int lines = 1;

    private void readingFile(String s) {
        String attr[] = s.split("、");
        //奇数行是类别，偶数行是上一行类别的元素
        if (lines % 2 != 0) {
            mGameSortData = new GameSortData(s);
        } else {
            int length = attr.length;
            ArrayList<String> elements = mGameSortData.getSortElements();
            Collections.addAll(elements, attr);
            mGameSortDataArrayList.add(mGameSortData);
        }
        lines += 1;
    }

    public ArrayList<GameSortData> getGameSortDataArrayList() {
        return mGameSortDataArrayList;
    }
}
