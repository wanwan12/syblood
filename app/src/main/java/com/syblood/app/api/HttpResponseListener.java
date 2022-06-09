package com.syblood.app.api;

import com.syblood.app.utils.LogUtil;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.Response;

import java.net.ProtocolException;

/**
 * Http返回监听
 * <p>
 * Created by xw on 2016-7-27.
 *
 * @param <T> 请求返回类型
 */
public class HttpResponseListener<T> implements OnResponseListener<T>
{

    /**
     * Request.
     */
    private Request<?> mRequest;

    /**
     * 结果回调.
     */
    private ApiRequestCallBack callback;

    /**
     * 是否销毁
     */
    private boolean mIsDestroy;

    /**
     * 构造函数
     *
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     */
    public HttpResponseListener(Request<?> request, ApiRequestCallBack httpCallback)
    {
        this.mRequest = request;

        this.callback = httpCallback;
    }

    @Override
    public void onStart(int what)
    {
        if (callback != null)
            callback.onPreStart();
    }

    @Override
    public void onFinish(int what)
    {
        if (callback != null)
            callback.onFinish();
    }

    @Override
    public void onSucceed(int what, Response<T> response)
    {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400)
        {
            if (responseCode == 405)
            {
                if (callback != null)
                {
                    callback.onFailure("405", "服务器不支持该请求方法");
                }
                return;
            }/* else
            {
                if (callback != null)
                    callback.onFailure(String.valueOf(responseCode), "");
            }*/
        }
        if (callback != null)
        {
            if (response.get() instanceof String)
            {
                LogUtil.i(response.get().toString());
                ApiResponseObject apiResponseObject = new ApiResponseObject(response.get().toString());
                if (apiResponseObject.isSuccess())
                {
                    callback.onSuccess(apiResponseObject);
                } else
                {
                    callback.onFailure(apiResponseObject.get_returnCode(), apiResponseObject.get_returnMsg());
                }
            }
        }
    }

    /**
     * When there was an error correction.
     *
     * @param what     the credit of the incoming request is used to distinguish between multiple requests.
     * @param response failure callback.
     */
    @Override
    public void onFailed(int what, Response<T> response)
    {
        String errorCode;
        String errotMsg;
        Exception exception = response.getException();
        if (exception instanceof NetworkError)
        {// 网络不好
            errorCode = "NetworkError";
            errotMsg = "网络出错，请检查网络。";
        } else if (exception instanceof TimeoutError)
        {// 请求超时
            errorCode = "TimeoutError";
            errotMsg = "请求超时，网络不好或者服务器不稳定。";
        } else if (exception instanceof UnKnownHostError)
        {// 找不到服务器
            errorCode = "UnKnownHostError";
            errotMsg = "未发现指定服务器，请切换网络后重试。";
        } else if (exception instanceof URLError)
        {// URL是错的
            errorCode = "URLError";
            errotMsg = "URL错误。";
        } else if (exception instanceof NotFoundCacheError)
        {// 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            errorCode = "NotFoundCacheError";
            errotMsg = "没有找到缓存。";
        } else if (exception instanceof ProtocolException)
        {//不支持的请求
            errorCode = "ProtocolException";
            errotMsg = "系统不支持的请求方法。";
        } else
        {
            errorCode = "UnKnow";
            errotMsg = "网络异常";
        }
        if (callback != null)
            callback.onFailure(errorCode, errotMsg);
    }
}
