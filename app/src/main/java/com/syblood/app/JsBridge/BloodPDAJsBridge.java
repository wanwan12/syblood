package com.syblood.app.JsBridge;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.syblood.app.application.AppContext;

/**
 * 输血PDA 前端JS调用函数，用于web页面与Android交互
 * <p>
 * Created by xw on 2022-01-14.
 */
public class BloodPDAJsBridge
{
    private Activity _activity;

    private WebView _webView;

    /**
     * 构造函数
     *
     * @param context  Context
     * @param mWebView WebView组件
     */
    public BloodPDAJsBridge(Activity context, WebView mWebView)
    {
        this._activity = context;
        this._webView = mWebView;
    }

    /**
     * 设置本地缓存
     *
     * @param key
     * @param value
     */
    @JavascriptInterface
    public void setLocCache(String key, String value)
    {
        AppContext.getInstance().set(key, value);
    }

    /**
     * 获取本地缓存
     *
     * @param key         缓存key
     * @param callbakFlag 回调标识
     */
    @JavascriptInterface
    public void getLocCache(String key, String callbakFlag)
    {
        String _value = AppContext.getInstance().get(key, "");

        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _webView.loadUrl("javascript:returnCache('" + key + "','" + _value + "','" + callbakFlag + "')");
            }
        });
    }
}
