package com.syblood.app.utils;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

import com.syblood.app.R;
import com.syblood.app.application.AppManager;
import com.syblood.app.ui.activities.MainActivity;


public class DoubleClickExitHelper
{
    private final Activity mActivity;

    private boolean isOnKeyBacking;
    private Handler mHandler;
    private Toast mBackToast;

    public DoubleClickExitHelper(Activity activity)
    {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onKeyDown事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode != KeyEvent.KEYCODE_BACK)
        {
            return false;
        }
        if (isOnKeyBacking)
        {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null)
            {
                mBackToast.cancel();
            }

            isOnKeyBacking=false;
            AppManager.create().finishOthersActivity(MainActivity.class);
            mActivity.moveTaskToBack(false);
            return true;
        } else
        {
            isOnKeyBacking = true;
            if (mBackToast == null)
            {
                mBackToast = Toast.makeText(mActivity, R.string.tip_double_click_exit, Toast.LENGTH_LONG);
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            isOnKeyBacking = false;
            if (mBackToast != null)
            {
                mBackToast.cancel();
            }
        }
    };
}