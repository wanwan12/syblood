package com.syblood.app.api;

import android.app.Application;

import com.syblood.app.utils.StringUtil;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.util.Map;

/**
 * Http方式访问接口
 * <p>
 * Created by xw on 2016-7-21.
 */
public class ApiAccessHttp
{
    /**
     * 单例对象
     */
    private static ApiAccessHttp mApiAccessHttp;

    /**
     * 请求队列.
     */
    private RequestQueue mRequestQueue;

    /**
     * 请求url.
     */
    private String mUrl;

    /**
     * 请求授权码.
     */
    private String mKeyCode;

    /**
     * 构造方法
     * 其中初始化
     */
    private ApiAccessHttp()
    {
        mRequestQueue = NoHttp.newRequestQueue();
    }

    /**
     * 请求队列.
     */
    public synchronized static ApiAccessHttp getRequestInstance()
    {
        if (mApiAccessHttp == null)
            mApiAccessHttp = new ApiAccessHttp();
        return mApiAccessHttp;
    }

    /**
     * 设置默认访问url
     *
     * @param url 访问地址
     */
    public void setHttpURL(String url)
    {
        this.mUrl = url;
    }

    /**
     * 设置默认授权码
     *
     * @param keyCode 授权码
     */
    public void setKeyCode(String keyCode)
    {
        this.mKeyCode = keyCode;
    }

    /**
     * 设置默认访问url
     *
     * @param application Application
     */
    public static void initHttp(Application application)
    {
        NoHttp.initialize(application);
    }

    /**
     * 设置默认访问url
     *
     * @param isDebug 是否开启日志调试
     */
    public void setDebugHttp(boolean isDebug)
    {
        Logger.setDebug(isDebug);
    }

    /**
     * 设置默认连接超时时间
     *
     * @param timeout 超时时间
     */
    public void setDefConnectTimeout(int timeout)
    {
        NoHttp.initialize(NoHttp.getContext(), new NoHttp.Config()
                .setConnectTimeout(timeout) // 全局连接超时时间，单位毫秒。
        );
    }

    /**
     * 设置默认读取超时时间
     *
     * @param timeout 超时时间
     */
    public void setDefReadTimeout(int timeout)
    {
        NoHttp.initialize(NoHttp.getContext(), new NoHttp.Config()
                .setReadTimeout(timeout) // 全局连接超时时间，单位毫秒。
        );
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求对象
     * @param what          用来标志请求, 当多个请求使用同一个{@link ApiRequestCallBack}时, 在回调方法中会返回这个what.
     * @param request       请求对象.
     * @param callback      结果回调对象.
     */
    public void add(Object requestSource, int what, Request<String> request, ApiRequestCallBack callback)
    {
        mRequestQueue.add(what, request, new HttpResponseListener<String>(request, callback));
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource  请求发起对象
     * @param url            请求地址
     * @param requestObject  Api请求对象
     * @param callback       结果回调对象
     * @param isDestroy      是否销毁
     * @param isAES          是否加密请求
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String url, ApiRequestObject requestObject, ApiRequestCallBack callback, boolean isDestroy, boolean isAES, int connectTimeout, int readTimeout)
    {
        CommonRequest request = new CommonRequest(url);
        //request.add("json", requestObject.toJsonString(true, ""));
        request.setDefineRequestBodyForJson(requestObject.toJsonString(true, "Base64"));

        //设置取消标记
        request.setCancelSign(requestSource, isDestroy);

        //设置超时时间
        if (connectTimeout > 0)
        {
            request.setConnectTimeout(connectTimeout);
        }
        if (readTimeout > 0)
        {
            request.setReadTimeout(readTimeout);
        }

        this.add(requestSource, 0, request, callback);

        return request;
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource  请求发起对象
     * @param url            请求地址
     * @param keyCode        授权码
     * @param apiCode     业务代码
     * @param subsysNo   医院代码
     * @param paramsMap      参数
     * @param callback       结果回调对象
     * @param isDestroy      是否销毁
     * @param isAES          是否加密
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String url, String keyCode, String apiCode, String subsysNo, Map<String, Object> paramsMap, ApiRequestCallBack callback, boolean isDestroy, boolean isAES, int connectTimeout, int readTimeout)
    {
        if (!StringUtil.isEmpty(url))
        {
            ApiRequestObject requestObject = new ApiRequestObject(apiCode, keyCode, subsysNo, paramsMap);
            return apiAccess(requestSource, url, requestObject, callback, isDestroy, isAES, connectTimeout, readTimeout);
        } else
        {
            return null;
        }
    }

    /**
     * 添加一个请求到请求队列(默认加密).
     *
     * @param requestSource 请求发起对象
     * @param apiCode    业务代码
     * @param subsysNo  医院代码
     * @param paramsMap     参数
     * @param callback      结果回调对象
     * @param isDestroy     是否销毁
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String apiCode, String subsysNo, Map<String, Object> paramsMap, ApiRequestCallBack callback, boolean isDestroy)
    {
        return apiAccess(requestSource, mUrl, mKeyCode, apiCode, subsysNo, paramsMap, callback, isDestroy, true, 0, 0);
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求发起对象
     * @param apiCode    业务代码
     * @param subsysNo  医院代码
     * @param paramsMap     参数
     * @param callback      结果回调对象
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String apiCode, String subsysNo, Map<String, Object> paramsMap, ApiRequestCallBack callback)
    {
        return apiAccess(requestSource, apiCode, subsysNo, paramsMap, callback, true);
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求发起对象
     * @param apiCode    业务代码
     * @param paramsMap     参数
     * @param callback      结果回调对象
     * @param isDestroy     是否销毁
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String apiCode, Map<String, Object> paramsMap, ApiRequestCallBack callback, boolean isDestroy)
    {
        return apiAccess(requestSource, apiCode, "", paramsMap, callback, isDestroy);
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求发起对象
     * @param apiCode    业务代码
     * @param paramsMap     参数
     * @param callback      结果回调对象
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, String apiCode, Map<String, Object> paramsMap, ApiRequestCallBack callback)
    {
        return apiAccess(requestSource, apiCode, "", paramsMap, callback, true);
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求发起对象
     * @param url           请求地址
     * @param requestObject Api请求对象
     * @param callback      结果回调对象
     * @param isDestroy     是否销毁
     */
    public CommonRequest apiAccess(Object requestSource, String url, ApiRequestObject requestObject, ApiRequestCallBack callback, boolean isDestroy)
    {
        if (!StringUtil.isEmpty(url))
        {
            return apiAccess(requestSource, url, requestObject, callback, isDestroy, true, 0, 0);
        } else
        {
            return null;
        }
    }

    /**
     * 添加一个请求到请求队列.
     *
     * @param requestSource 请求发起对象
     * @param requestObject Api请求对象
     * @param callback      结果回调对象
     * @param isDestroy     是否销毁
     * @return
     */
    public CommonRequest apiAccess(Object requestSource, ApiRequestObject requestObject, ApiRequestCallBack callback, boolean isDestroy)
    {
        return apiAccess(requestSource, mUrl, requestObject, callback, isDestroy);
    }

    /**
     * 添加一个请求到请求队列.
     * 默认销毁
     *
     * @param requestSource 请求发起对象
     * @param requestObject Api请求对象
     * @param callback      结果回调对象.
     * @return YlwjRequest
     */
    public CommonRequest apiAccess(Object requestSource, ApiRequestObject requestObject, ApiRequestCallBack callback)
    {
        return apiAccess(requestSource, mUrl, requestObject, callback, true, true, 0, 0);
    }

    /**
     * 取消这个sign标记的所有请求.
     */
    public void cancelBySign(Object sign)
    {
        mRequestQueue.cancelBySign(sign);
    }

    /**
     * 取消队列中所有请求.
     */
    public void cancelAll()
    {
        mRequestQueue.cancelAll();
    }

    /**
     * 退出app时停止所有请求.
     */
    public void stopAll()
    {
        mRequestQueue.stop();
    }
}
