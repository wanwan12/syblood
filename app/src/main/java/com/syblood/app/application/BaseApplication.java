package com.syblood.app.application;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syblood.app.R;
import com.syblood.app.api.ApiAccessHttp;
import com.syblood.app.utils.ToastUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Application基类
 * <p/>
 * Created by xw on 2016-7-27.
 */
public class BaseApplication extends Application
{
    /**
     * 当前Context
     */
    private static Context SContext;

    /**
     * 资源
     */
    private static Resources SResource;

    /**
     * SharedPreferencesName
     */
    private static String PREF_NAME = "app.pref";

    /**
     * 是否 9以前
     */
    private static boolean SIsAtLeastGB;

    static
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            SIsAtLeastGB = true;
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        SContext = getApplicationContext();
        SResource = SContext.getResources();
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        ApiAccessHttp.initHttp(this);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if(processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName()))
        {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
    }

    private String getAppName(int pID)
    {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while(i.hasNext())
        {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try
            {
                if(info.pid == pID)
                {
                    processName = info.processName;
                    return processName;
                }
            }
            catch(Exception e)
            {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        //取消所有任务
        ApiAccessHttp.getRequestInstance().cancelAll();
    }

    /**
     * 返回当前Context
     *
     * @return BaseApplication
     */
    public static synchronized BaseApplication context()
    {
        return (BaseApplication) SContext;
    }

    /**
     * 返回资源
     *
     * @return Resources
     */
    public static Resources resources()
    {
        return SResource;
    }

    /**
     * 提交SharedPreferences修改
     *
     * @param editor SharedPreferences.Editor
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor)
    {
        if(SIsAtLeastGB)
        {
            editor.apply();
        }
        else
        {
            editor.commit();
        }
    }

    /**
     * SharedPreferences 添加数据 int
     *
     * @param key   key
     * @param value Value
     */
    public static void set(String key, int value)
    {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    /**
     * SharedPreferences 添加数据 boolean
     *
     * @param key   key
     * @param value Value
     */
    public static void set(String key, boolean value)
    {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    /**
     * SharedPreferences 添加数据 String
     *
     * @param key   key
     * @param value Value
     */
    public static void set(String key, String value)
    {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    /**
     * SharedPreferences 添加数据 long
     *
     * @param key   key
     * @param value Value
     */
    public static void set(String key, long value)
    {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        apply(editor);
    }

    /**
     * SharedPreferences 添加数据 float
     *
     * @param key   key
     * @param value Value
     */
    public static void set(String key, float value)
    {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        apply(editor);
    }

    /**
     * SharedPreferences 获取值 boolean
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static boolean get(String key, boolean defValue)
    {
        return getPreferences().getBoolean(key, defValue);
    }

    /**
     * SharedPreferences 获取值 String
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static String get(String key, String defValue)
    {
        return getPreferences().getString(key, defValue);
    }

    /**
     * SharedPreferences 获取值 int
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static int get(String key, int defValue)
    {
        return getPreferences().getInt(key, defValue);
    }

    /**
     * SharedPreferences 获取值 long
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static long get(String key, long defValue)
    {
        return getPreferences().getLong(key, defValue);
    }

    /**
     * SharedPreferences 获取值 float
     *
     * @param key      key
     * @param defValue 默认值
     * @return
     */
    public static float get(String key, float defValue)
    {
        return getPreferences().getFloat(key, defValue);
    }

    /**
     * 获取SharedPreferences
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences()
    {
        SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
                Context.MODE_MULTI_PROCESS);
        return pre;
    }

    /**
     * 获取SharedPreferences
     *
     * @param prefName SharedPreferencesName
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String prefName)
    {
        return context().getSharedPreferences(prefName,
                Context.MODE_MULTI_PROCESS);
    }

    /**
     * 获取String资源
     *
     * @param id ID
     * @return
     */
    public static String string(int id)
    {
        return SResource.getString(id);
    }

    /**
     * 获取String资源 转义符
     *
     * @param id   ID
     * @param args 值
     * @return
     */
    public static String string(int id, Object... args)
    {
        return SResource.getString(id, args);
    }

    /**
     * 显示Toast
     *
     * @param message
     */
    public static void showToast(int message)
    {
        showToast(message, Toast.LENGTH_LONG, 0);
    }

    public static void showToast(String message)
    {
        showToast(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void showToast(int message, int icon)
    {
        showToast(message, Toast.LENGTH_LONG, icon);
    }

    public static void showToast(String message, int icon)
    {
        showToast(message, Toast.LENGTH_LONG, icon, Gravity.BOTTOM);
    }

    public static void showToastShort(int message)
    {
        showToast(message, Toast.LENGTH_SHORT, 0);
    }

    public static void showToastShort(String message)
    {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    public static void showToastShort(int message, Object... args)
    {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
    }

    public static void showToast(int message, int duration, int icon)
    {
        showToast(message, duration, icon, Gravity.BOTTOM);
    }

    public static void showToast(int message, int duration, int icon,
                                 int gravity)
    {
        showToast(context().getString(message), duration, icon, gravity);
    }

    public static void showToast(int message, int duration, int icon,
                                 int gravity, Object... args)
    {
        showToast(context().getString(message, args), duration, icon, gravity);
    }

    public static void showToast(String message, int duration, int icon,
                                 int gravity)
    {
        ToastUtil.show(context(), message, duration, icon);
    }

    public static void showRxscToast(String info) {

        Toast toast = Toast.makeText(context(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout layout = (LinearLayout) toast.getView();
        layout.setBackgroundColor(Color.parseColor("#40111111"));
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(context().getResources().getColor(R.color.colorFFFFFF));
        v.setTextSize(context().getResources().getDimensionPixelSize(R.dimen.LIB_PX30));
        v.setPadding(10,10,10,10);
        toast.setText(info);
        toast.show();

    }
}
