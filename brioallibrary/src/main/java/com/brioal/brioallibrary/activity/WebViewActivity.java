package com.brioal.brioallibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.brioal.brioallibrary.R;
import com.brioal.brioallibrary.util.StatusBarUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    Toolbar mToolbar;
    WebView mWebView;
    SwipeRefreshLayout refreshLayout;
    CoordinatorLayout mContainer;

    private String mTitle;
    private String mUrl;
    Context mContext;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //显示网络超时
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
                Snackbar.make(mContainer, "网络暂时不可用,请稍后再试", Snackbar.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        mContext = this;
        initId();
        initData();
        initBar();
        initRefreshLayout();
        initView();
    }

    private void initId() {
        mToolbar = (Toolbar) findViewById(R.id.web_toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.web_refresh);
        mWebView = (WebView) findViewById(R.id.web_webView);
    }

    private void initBar() {
        mToolbar.setTitle(mTitle);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        StatusBarUtils.setColor(this,getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
    }

    private void initView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress != 100) {
                    refreshLayout.setRefreshing(true);
                } else {
                    refreshLayout.setRefreshing(false);
                }


            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mTitle == null) {
                    mTitle = title;
                    mToolbar.setTitle(title);
                    Intent intent = new Intent();
                    intent.putExtra("mUrl", mUrl);
                    intent.putExtra("mTitle", mTitle);
                    setResult(RESULT_OK, intent);
                }
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);  //支持js
        settings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.supportMultipleWindows();  //多窗口
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        saveData(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.loadUrl(mUrl);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.loadUrl(mUrl);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mHander.sendEmptyMessage(0);
                    }
                }, 9000);
            }
        });
    }

    private void saveData(WebSettings mWebSettings) {
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    WebViewClient webViewClient = new WebViewClient() {

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    };

    WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    private void initData() {
        mUrl = getIntent().getStringExtra("Url");
        mTitle = getIntent().getStringExtra("Title");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);
        //装填R.menu.my_menu对应的菜单,并添加到menu中
        inflator.inflate(R.menu.menu_web, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
//
//            case R.id.action_up:
//                mWebView.scrollTo(0, 0);
//                break;
//            case R.id.action_copy:
//                ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                c.setText(mUrl);//设置Clipboard 的内容
//                Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.action_share:
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
//                sendIntent.setType("text/plain");
//                startActivity(Intent.createChooser(sendIntent, "选择要分享的方式"));
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
