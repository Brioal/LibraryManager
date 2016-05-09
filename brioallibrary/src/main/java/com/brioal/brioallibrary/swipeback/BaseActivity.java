package com.brioal.brioallibrary.swipeback;

import android.support.v7.app.AppCompatActivity;

import com.brioal.brioallibrary.R;
import com.brioal.brioallibrary.util.StatusBarUtils;


/**
 * Created by Jaeger on 16/2/14.
 * StatusBarDemo
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

}
