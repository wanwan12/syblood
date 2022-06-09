package com.syblood.app.application;

/**
 * Http异常
 * <p/>
 * Created by xw on 2016-7-30.
 */
public class HttpException extends Exception
{
    public HttpException()
    {
    }

    public HttpException(String exceptionMessage)
    {
        super(exceptionMessage);
    }

    public HttpException(String exceptionMessage, Throwable reason)
    {
        super(exceptionMessage);
        initCause(reason);
    }
}
