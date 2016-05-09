package com.brioal.librarymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.brioal.brioallibrary.activity.WebViewActivity;
import com.brioal.brioallibrary.citypicker.CityPickerActivity;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cityPicker(View view) {
        startActivity(new Intent(MainActivity.this, CityPickerActivity.class));
    }

    public void webView(View view) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("Url", "http://www.baidu.com");
        intent.putExtra("Title", "http：//www.baidu.com");
        startActivity(intent);
    }

    public void swipeBack(View view) {
        startActivity(new Intent(MainActivity.this,SwipeBackTestActivity.class));
    }


}
