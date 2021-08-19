package com.oldman.permission.common.util;

import java.util.*;

/**
 * 数组工具类
 * @author oldman
 * @date 2021/8/19 22:32
 */
public class ArrayUtils {
    /**
     * 并集（set唯一性）
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] union (String[] arr1, String[] arr2){
        Set<String> hs = new HashSet<String>();
        for(String str:arr1){
            hs.add(str);
        }
        for(String str:arr2){
            hs.add(str);
        }
        String[] result={};
        return hs.toArray(result);
    }

    /**
     * 交集(注意结果集中若使用LinkedList添加，则需要判断是否包含该元素，否则其中会包含重复的元素)
     * @param arr1
     * @param arr2
     * @return
     */
    public static String[] intersect(String[] arr1, String[] arr2){
        List<String> l = new LinkedList<String>();
        Set<String> common = new HashSet<String>();
        for(String str:arr1){
            if(!l.contains(str)){
                l.add(str);
            }
        }
        for(String str:arr2){
            if(l.contains(str)){
                common.add(str);
            }
        }
        String[] result={};
        return common.toArray(result);
    }

    /**
     * 差集
     * @param arr1 长度大
     * @param arr2 长度小
     * @return
     */
    public static Long[] substract(Long[] arr1, Long[] arr2) {
        LinkedList<Long> list = new LinkedList<Long>();
        for (Long str : arr1) {
            if(!list.contains(str)) {
                list.add(str);
            }
        }
        for (Long str : arr2) {
            if (list.contains(str)) {
                list.remove(str);
            }
        }
        Long[] result = {};
        return list.toArray(result);
    }
}
