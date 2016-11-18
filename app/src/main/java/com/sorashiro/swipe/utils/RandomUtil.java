package com.sorashiro.swipe.utils;

import java.util.HashSet;

/**
 * @author Sora
 * @date 2016/11/6
 *
 * 跟随机相关的工具类
 *
 */

public class RandomUtil {

    /**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     * ！！可能会递归过多导致没有存放足够的随机数！！
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     * @param HashSet<Integer> set 随机数结果集
     */

    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);
        }
        int setSize = set.size();
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     */
    public static int[] RandomCommon(int min, int max, int n){
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while(count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if(num == result[j]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                result[count] = num;
                count++;
            }
        }
        return result;
    }

}
