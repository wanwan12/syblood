package com.syblood.app.ui.activities.able;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 规范Activity跳转的接口协议
 * <p>
 * Created by xw on 2016-7-28.
 */
public interface I_SkipActivity
{
    /**
     * 跳转并结束Activity
     *
     * @param aty Activity
     * @param cls Class
     */
    void skipActivity(Activity aty, Class<?> cls);

    /**
     * 跳转并结束Activity
     *
     * @param aty Activity
     * @param it  Intent
     */
    void skipActivity(Activity aty, Intent it);

    /**
     * 跳转并结束Activity 传递参数
     *
     * @param aty    Activity
     * @param cls    Class
     * @param extras Bundle
     */
    void skipActivity(Activity aty, Class<?> cls, Bundle extras);

    /**
     * 跳转
     *
     * @param aty Activity
     * @param cls Class
     */
    void showActivity(Activity aty, Class<?> cls);

    /**
     * 跳转 指定 Intent
     *
     * @param aty Activity
     * @param it  Intent
     */
    void showActivity(Activity aty, Intent it);

    /**
     * @param aty
     * @param cls
     * @param extras
     */
    void showActivity(Activity aty, Class<?> cls, Bundle extras);

    /**
     * ForResult方式跳转 指定requestCode
     *
     * @param aty
     * @param cls
     * @param requestCode
     */
    void showForResultActivity(Activity aty, Class<?> cls, int requestCode);

    /**
     * ForResult方式跳转 指定requestCode 传递参数
     *
     * @param aty         Activity
     * @param cls         Class
     * @param requestCode RequestCode
     * @param extras      Bundle
     */
    void showForResultActivity(Activity aty, Class<?> cls, int requestCode, Bundle extras);
}
