package com.syblood.app.ui.activities.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.syblood.app.R;
import com.syblood.app.api.ApiAccessHttp;
import com.syblood.app.application.AppManager;
import com.syblood.app.ui.activities.able.I_BaseActivity;
import com.syblood.app.ui.activities.able.I_SkipActivity;
import com.syblood.app.ui.activities.able.I_WaitDialog;
import com.syblood.app.ui.activities.dialog.LoadingDialog;
import com.syblood.app.utils.LogUtil;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;

/**
 * Activity基类
 * <p/>
 * Created by xw on 2016-7-27.
 */
public class BaseActivity extends FragmentActivity implements I_BaseActivity, I_SkipActivity,
        I_WaitDialog
{
    /**
     * 当前Activity
     */
    public Activity aty;
    /**
     * Activity状态
     */
    public int activityState = DESTROY;
    /**
     * 加载进度
     */
    private LoadingDialog mLoadingDialog;

    @Override
    public void setRootView() {
        setContentView(getLayoutId());
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    /**
     * 初始化，包含initData  initActionBar  initWidget
     */
    public void initializer() {
        initData();
        initActionBar();
        initWidget();
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        aty = this;
        AppManager.create().addActivity(this);
        LogUtil.state(this.getClass().getName(), "---------onCreat ");
        super.onCreate(savedInstanceState);

        setRootView();
        ButterKnife.bind(this);
        initializer();
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.state(this.getClass().getName(), "---------onStart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = RESUME;
        LogUtil.state(this.getClass().getName(), "---------onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = PAUSE;
        LogUtil.state(this.getClass().getName(), "---------onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = STOP;
        LogUtil.state(this.getClass().getName(), "---------onStop ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.state(this.getClass().getName(), "---------onRestart ");
    }

    @Override
    protected void onDestroy() {
        activityState = DESTROY;
        LogUtil.state(this.getClass().getName(), "---------onDestroy ");
        super.onDestroy();
        //取消Http任务  可销毁的在Sign后面增加标记
        ApiAccessHttp.getRequestInstance().cancelBySign(this.toString() + "-1");
        //结束当前
        AppManager.create().finishActivity(this);
        aty = null;
    }
    /***************************************************************************
     * 弹出窗方法
     ***************************************************************************/
    /**
     * 隐藏
     */
    @Override
    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 显示（默认不可取消）
     *
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog() {
        return showWaitDialog(getString(R.string.loading_dialog_message), false, null);
    }

    /**
     * 显示（默认不可取消）
     *
     * @param resid 消息Id
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(@StringRes int resid) {
        String message = getString(resid);
        return showWaitDialog(message, false, null);
    }

    /**
     * 显示（默认不可取消）
     *
     * @param message 消息
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(String message) {
        return showWaitDialog(message, false, null);
    }

    /**
     * 显示
     *
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        return showWaitDialog(getString(R.string.loading_dialog_message), isCancel, cancelListener);
    }

    /**
     * 显示
     *
     * @param resid          资源Id
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(@StringRes int resid, boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        String message = getString(resid);
        return showWaitDialog(message, isCancel, cancelListener);
    }

    /**
     * 显示
     *
     * @param message        消息
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    @Override
    public LoadingDialog showWaitDialog(String message, boolean isCancel, DialogInterface.OnCancelListener cancelListener) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }

        mLoadingDialog.setCancelable(isCancel);
        if (isCancel == true && cancelListener != null) {
            mLoadingDialog.setOnCancelListener(cancelListener);
        }

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }

        return mLoadingDialog;
    }

    /***************************************************************************
     * 跳转相关方法
     ***************************************************************************/
    /**
     * 跳转并结束Activity
     *
     * @param aty Activity
     * @param cls Class
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls) {
        showActivity(aty, cls);
        aty.finish();
    }

    /**
     * 跳转并结束Activity
     *
     * @param aty Activity
     * @param it  Intent
     */
    @Override
    public void skipActivity(Activity aty, Intent it) {
        showActivity(aty, it);
        aty.finish();
    }

    /**
     * 跳转并结束Activity 传递参数
     *
     * @param aty    Activity
     * @param cls    Class
     * @param extras Bundle
     */
    @Override
    public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
        showActivity(aty, cls, extras);
        aty.finish();
    }

    /**
     * 跳转
     *
     * @param aty Activity
     * @param cls Class
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * 跳转 指定 Intent
     *
     * @param aty Activity
     * @param it  Intent
     */
    @Override
    public void showActivity(Activity aty, Intent it) {
        aty.startActivity(it);
    }

    /**
     * @param aty
     * @param cls
     * @param extras
     */
    @Override
    public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClass(aty, cls);
        aty.startActivity(intent);
    }

    /**
     * ForResult方式跳转 指定requestCode
     *
     * @param aty
     * @param cls
     * @param requestCode
     */
    @Override
    public void showForResultActivity(Activity aty, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        aty.startActivityForResult(intent, requestCode);
    }

    /**
     * ForResult方式跳转 指定requestCode
     *
     * @param aty         Activity
     * @param intent      Intent
     * @param requestCode 请求代码
     */
    public void showForResultActivity(Activity aty, Intent intent, int requestCode) {
        aty.startActivityForResult(intent, requestCode);
    }

    /**
     * ForResult方式跳转 指定requestCode 传递参数
     *
     * @param aty         Activity
     * @param cls         Class
     * @param requestCode RequestCode
     * @param extras      Bundle
     */
    @Override
    public void showForResultActivity(Activity aty, Class<?> cls, int requestCode, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(aty, cls);
        intent.putExtras(extras);
        aty.startActivityForResult(intent, requestCode);
    }
}
