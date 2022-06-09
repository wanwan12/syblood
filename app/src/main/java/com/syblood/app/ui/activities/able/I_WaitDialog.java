package com.syblood.app.ui.activities.able;

import android.content.DialogInterface;

import com.syblood.app.ui.activities.dialog.LoadingDialog;

import androidx.annotation.StringRes;


/**
 * 等待对话框
 * <p>
 * Created by xw on 2016-7-28.
 */
public interface I_WaitDialog
{
    /**
     * 隐藏WaitDialog
     */
    void hideWaitDialog();

    /**
     * 显示
     *
     * @return
     */
    LoadingDialog showWaitDialog();

    /**
     * 显示
     *
     * @param resid 消息
     * @return
     */
    LoadingDialog showWaitDialog(@StringRes int resid);

    /**
     * 显示
     *
     * @param message 消息
     * @return
     */
    LoadingDialog showWaitDialog(String message);

    /**
     * 显示
     *
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    LoadingDialog showWaitDialog(boolean isCancel, DialogInterface.OnCancelListener cancelListener);

    /**
     * 显示
     *
     * @param resid          资源Id
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    LoadingDialog showWaitDialog(@StringRes int resid, boolean isCancel, DialogInterface.OnCancelListener cancelListener);

    /**
     * @param message        消息
     * @param isCancel       是否可取消
     * @param cancelListener 取消监听
     * @return
     */
    LoadingDialog showWaitDialog(String message, boolean isCancel, DialogInterface.OnCancelListener cancelListener);
}
