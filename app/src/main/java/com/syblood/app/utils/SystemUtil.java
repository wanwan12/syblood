package com.syblood.app.utils;

import android.Manifest;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * 系统工具类
 * <p>
 * Created by xw on 2016-7-14.
 */
public class SystemUtil
{
    static Handler mHandler = new Handler();

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }

    /**
     * 判断是否为wifi联网
     */
    public static boolean isWiFi(Context cxt)
    {
        //Context context = cxt.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null)
            {
                for(int i = 0; i < info.length; i++)
                {
                    if(info[i].getTypeName().equals("WIFI") && info[i].isConnected())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取异常堆栈信息
     *
     * @param tr 异常
     * @return Stack trace in form of String
     */
    static String getStackTraceString(Throwable tr)
    {
        if(tr == null)
        {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while(t != null)
        {
            if(t instanceof UnknownHostException)
            {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * 当前是否处于锁屏
     *
     * @return
     */
    public static boolean isScreenLocked(Context context)
    {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isLocked = mKeyguardManager.inKeyguardRestrictedInputMode();
        return isLocked;
    }

    /**
     * 当前屏幕是否亮着
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH)
        {
            return pm.isScreenOn();
        }
        else
        {
            return pm.isInteractive();
        }
    }

    /**
     * 点亮屏幕
     *
     * @param context
     * @param delay   多少毫秒后熄屏
     */
    public static void ScreenOn(Context context, long delay)
    {
        if(!isScreenOn(context))
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG");
            wakeLock.acquire();
            if(delay > 0)
            {
                mHandler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        wakeLock.release();
                    }
                }, delay);
            }
        }
    }

	/**
	 * 通过用户ID 获得用户头像地址
     * @param userId
     */
    public static String getHeadFromId(String userId) {
        String userHead = "";
        if(userId==null){
            userId="";
        }
        if (userId.startsWith("E") || userId.startsWith("e") || userId.startsWith("d")) {
            userHead = "/headPic/D" + userId.substring(1, userId.length()) + ".jpg";
        } else {
            userHead = "/headPic/" + userId + ".jpg";
        }
		return userHead;
    }
}
