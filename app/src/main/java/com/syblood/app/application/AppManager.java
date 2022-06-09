package com.syblood.app.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.syblood.app.ui.activities.able.I_BaseActivity;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * <p/>
 * Created by xw on 2016-7-28.
 */
public class AppManager
{
    private static Stack<I_BaseActivity> activityStack;
    private static final AppManager instance = new AppManager();

    private AppManager() {
    }

    /**
     * 创建AppManager
     *
     * @return
     */
    public static AppManager create()
    {
        return instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount()
    {
        return activityStack.size();
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(I_BaseActivity activity)
    {
        if (activityStack == null)
        {
            activityStack = new Stack<I_BaseActivity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity topActivity()
    {
        if (activityStack == null)
        {
            return null;
        }
        if (activityStack.isEmpty())
        {
            return null;
        }
        I_BaseActivity activity = activityStack.lastElement();
        return (Activity) activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls)
    {
        I_BaseActivity activity = null;
        for (I_BaseActivity aty : activityStack)
        {
            if (aty.getClass().equals(cls))
            {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity()
    {
        I_BaseActivity activity = activityStack.lastElement();
        finishActivity((Activity) activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity)
    {
        if (activity != null)
        {
            activityStack.remove(activity);
            // activity.finish();//此处不用finish
            activity = null;
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls)
    {
		Iterator<I_BaseActivity> iterator=activityStack.iterator();
		while(iterator.hasNext()){
			I_BaseActivity activity=iterator.next();
			if ((activity.getClass().equals(cls)))
			{
				iterator.remove();
				((Activity) activity).finish();
			}
		}
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls)
    {
		Iterator<I_BaseActivity> iterator=activityStack.iterator();
		while(iterator.hasNext()){
			I_BaseActivity activity=iterator.next();
			if (!(activity.getClass().equals(cls)))
			{
				iterator.remove();
				((Activity) activity).finish();
			}
		}

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity()
    {
        if (activityStack != null && activityStack.size() > 0)
        {
			Iterator<I_BaseActivity> iterator=activityStack.iterator();
			while(iterator.hasNext()){
				I_BaseActivity activity=iterator.next();
				iterator.remove();
				((Activity) activity).finish();

			}
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void AppExit(Context context)
    {
        try
        {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e)
        {
            Runtime.getRuntime().exit(-1);
        }
    }

    /**
     *判断当前应用程序处于前台还是后台
     *
     * @param context

     * @return
     */
	public  boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

					return true;
				}else{

					return false;
				}
			}
		}
		return false;
	}


    /**
     * 是否存在指定cls
     *
     * @param cls
     * @return
     */
    public boolean isExistActivity(Class<?> cls)
    {
        Iterator<I_BaseActivity> iterator=activityStack.iterator();
        while(iterator.hasNext()){
            I_BaseActivity activity=iterator.next();
            if ((activity.getClass().equals(cls)))
            {
                return true;
            }
        }
        return  false;
    }
}
