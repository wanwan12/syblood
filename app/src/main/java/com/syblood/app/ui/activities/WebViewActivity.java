package com.syblood.app.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.syblood.app.JsBridge.BloodPDAJsBridge;
import com.syblood.app.R;
import com.syblood.app.application.AppContext;
import com.syblood.app.constants.Constants;
import com.syblood.app.ui.activities.base.BaseActivity;
import com.syblood.app.utils.DoubleClickExitHelper;
import com.syblood.app.utils.MyJavascriptInterface;

import java.io.File;
import java.util.Random;

public class WebViewActivity extends BaseActivity
{
    private static android.webkit.WebView webView;

    private IntentFilter intentFilter;
    private BarcodeReciver barcodeReceiver;
    private String barcode;

    private static final String BROADCAST_NAME = "SYSTEM_BAR_READ";
    private static final String BROADCAST_NAME_D8 = "SYSTEM_BAR_READ";

    DoubleClickExitHelper mDoubleClickExit;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_webview;
    }

    @Override
    public void initActionBar()
    {
        super.initActionBar();
    }

    @Override
    public void initData()
    {
        super.initData();

        webView = (android.webkit.WebView) findViewById(R.id.webView);
        webView.requestFocusFromTouch();
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //初始化交互JS接口
        webView.addJavascriptInterface(new BloodPDAJsBridge(this, webView), "ADJS");
        webSettings.setJavaScriptEnabled(true);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });

        //webView.addJavascriptInterface(new MyJavascriptInterface(this, webView), "injectedObject");
        //webView.loadUrl("http://192.168.3.23:8899/HTMLPage1.htm?v=" + String.valueOf(new Random().nextInt()));

        //武宁人民医院移动护理地址
        //webView.loadUrl("http://10.8.10.204:8066/syblood_nurse/login?v=" + String.valueOf(new Random().nextInt()));
        //谢干东本地测试
        //webView.loadUrl("http://172.20.10.3:9090/syblood_nurse/login?v=" + String.valueOf(new Random().nextInt()));
        //赣州本地测试
        webView.loadUrl("http://155.6.16.75:9090/blood_nurse/login?v=" + String.valueOf(new Random().nextInt()));

        //webView.loadUrl("http://192.168.3.23:8899/test.htm?v=" + String.valueOf(new Random().nextInt()));

        //注册广播，添加ACTION
        //register broad ,add action
        intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_NAME );
        intentFilter.addAction(BROADCAST_NAME_D8 );
        barcodeReceiver = new BarcodeReciver();

        mDoubleClickExit = new DoubleClickExitHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(barcodeReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(barcodeReceiver);
    }

    //获取二维码
    //Get qr code
    private void getBarcode(Context context,Intent intent){
        if (intent.getAction().equals(BROADCAST_NAME)) {
            barcode = intent.getStringExtra("BAR_VALUE");
            Log.d("rdata", barcode);
        } else {
            barcode = intent.getStringExtra("EXTRA_SCAN_DATA");
            Log.d("EXTRA_SCAN_DATA", barcode);
        }
        int size = barcode.length();
        if(size != 0){
            AppContext.showToast(barcode);
            Log.d("code", barcode);
            webView.loadUrl("javascript:returnBarCode('" + barcode + "')");
        }
    }

    //广播监听
    public  class BarcodeReciver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent){
            getBarcode(context,intent);
        }
    }

    /**
     * 监听返回时间，二次点击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 是否退出应用
            if (AppContext.get(Constants.KEY_DOUBLE_CLICK_EXIT, true))
            {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        webView.clearCache(true);

        super.onDestroy();
        System.gc();
    }
}