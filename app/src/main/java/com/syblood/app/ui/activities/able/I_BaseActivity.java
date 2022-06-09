package com.syblood.app.ui.activities.able;

/**
 * BaseActivity接口协议，实现此接口可使用ActivityManager堆栈
 * <p>
 * Created by xw on 2016-7-28.
 */
public interface I_BaseActivity
{
    int DESTROY = 0;
    int PAUSE = 1;
    int STOP = 2;
    int RESUME = 3;

    /**
     * 设置root界面
     */
    void setRootView();

    /**
     * 获取布局文件
     *
     * @return
     */
    int getLayoutId();

    /**
     * 初始化ActionBar
     */
    void initActionBar();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化控件
     */
    void initWidget();
}
