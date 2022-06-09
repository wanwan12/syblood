package com.syblood.app.ui.activities;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.syblood.app.R;
import com.syblood.app.application.AppContext;
import com.syblood.app.ui.activities.base.BaseActivity;
import com.syblood.app.utils.SystemUtil;

public class WelcomeActivity extends BaseActivity
{
    ImageView ivStart;

    @Override
    public void setRootView() {
        final View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);

        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(800);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0)
            {
                /*if (!SystemUtil.checkNet(WelcomeActivity.this))
                {
                    AppContext.showToast("网络异常，请检查网络连接");
                    skipActivity(aty, MainActivity.class);
                    return;
                }*/
                //判断是否存在URL地址

                //进入主页
                //skipActivity(aty, WebViewActivity.class);
                skipActivity(aty, MainActivity.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationStart(Animation animation)
            {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}