package com.brioal.brioallibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by brioal on 16-4-16.
 */
public class BrioalUtil {
    public static void init(Context mContent) {
        SharedPreferences preferences = mContent.getSharedPreferences("Brioal", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = preferences.edit();
        // 已做笔记 未做笔记 已上架 等待上架
        editor.putString("Desc", "高仿QQ顶部选择栏").apply();
        editor.putString("Tag", "View,QQ").apply();
        editor.putString("State", "未做笔记").apply();
//        editor.putString("Url", "http://blog.csdn.net/qq_26971803/article/details/50984732").apply();

    }


}
