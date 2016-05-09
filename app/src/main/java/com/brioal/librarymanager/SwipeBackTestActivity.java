package com.brioal.librarymanager;

import android.os.Bundle;

import com.brioal.brioallibrary.swipeback.app.SwipeBackActivity;

public class SwipeBackTestActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back_test);
//        StatusBarUtils.setColor(this,getResources().getColor(R.color.colorPrimary));
    }
}
