package com.syblood.app.api;


import com.yanzhenjie.nohttp.IBasicRequest;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

/**
 * 自定义请求
 * <p/>
 * Created by xw on 2016-7-27.
 */
public class CommonRequest extends StringRequest
{
    /**
     * 取消标记.
     */
    private Object mCancelSign;

    /**
     * 自定义请求 默认POST请求方式
     * 构造函数
     *
     * @param url 访问url
     */
    public CommonRequest(String url)
    {
        this(url, RequestMethod.POST);
    }

    /**
     * 自定义请求
     * 构造函数
     *
     * @param url           访问url
     * @param requestMethod request方式
     */
    public CommonRequest(String url, RequestMethod requestMethod)
    {
        super(url, requestMethod);
    }

    /**
     * 设置取消标记
     *
     * @param sign      标记
     * @param isDestroy 是否销毁
     */
    public void setCancelSign(Object sign, boolean isDestroy)
    {
        setCancelSign(sign.toString() + (isDestroy ? "-1" : ""));
    }


    @Override
    public IBasicRequest setCancelSign(Object mCancelSign)
    {
        this.mCancelSign = mCancelSign;
        return this;
    }

    /**
     * 获取取消标记
     *
     * @return
     */
    public Object getCancelSign()
    {
        return this.mCancelSign;
    }

    @Override
    public void cancelBySign(Object sign)
    {
        if (sign instanceof String)
        {
            if (mCancelSign.equals(sign))
                cancel();
        } else
        {
            if (mCancelSign == sign)
                cancel();
        }

    }
}
