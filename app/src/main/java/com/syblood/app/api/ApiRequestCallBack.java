package com.syblood.app.api;

/**
 * Api访问请求回调类
 * <p/>
 * Created by xw on 2016-7-27.
 */
public abstract class ApiRequestCallBack
{
    /**
     * 请求开始之前回调
     */
    public void onPreStart()
    {
    }

    /**
     * 请求成功时回调
     *
     * @param t Request返回信息
     */
    public void onSuccess(ApiResponseObject t)
    {
    }

    /**
     * 请求失败时回调
     *
     * @param errorCode 错误代码
     * @param strMsg    错误原因
     */
    public void onFailure(String errorCode, String strMsg)
    {
    }

    /**
     * 请求结束后回调
     */
    public void onFinish()
    {
    }
}
