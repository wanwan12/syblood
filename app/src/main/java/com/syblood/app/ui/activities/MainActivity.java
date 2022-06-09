package com.syblood.app.ui.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.syblood.app.R;
import com.syblood.app.application.AppContext;
import com.syblood.app.constants.Constants;
import com.syblood.app.ui.activities.base.BaseActivity;
import com.syblood.app.ui.activities.dialog.InformationDialog;
import com.syblood.app.ui.activities.dialog.ProgressDialog;
import com.syblood.app.utils.AppUpdater;
import com.syblood.app.utils.DoubleClickExitHelper;
import com.syblood.app.utils.PermisionUtil;
import com.syblood.app.utils.SharedPreferencesUtil;
import com.syblood.app.utils.SystemUtil;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;
import java.util.List;

import androidx.core.content.FileProvider;


public class MainActivity extends BaseActivity implements AppUpdater.OnNewVersionListener, AppUpdater.OnNoNewVersionListener, AppUpdater.OnCompleteListener, AppUpdater.OnProgressListener
{
    public static Activity instance;
    public AppUpdater appUpdater = null;

    DoubleClickExitHelper mDoubleClickExit;
    Handler mhandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bld = msg.getData();
            switch (msg.what)
            {
                case 101:
                    final String appName = bld.getString("appName");

                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(MainActivity.this.getExternalFilesDir(""), appName)), "application/vnd.android.package-archive");
                    startActivity(intent);*/

                    reSetupApk(AppContext.getInstance(), MainActivity.this.getExternalFilesDir("").getPath(), appName);

                    break;
            }
        }
    };
    DownloadRequest downloadRequest;
    private List<TextView> textViewList;

    /**
     * 是否后台下载
     */
    private boolean isBackground = false;
    /**
     * 下载进度百分比
     */
    private long downloadPercent = 0;
    /**
     * 后台下载时通知栏对象
     */
    private Notification notification;
    private Notification.Builder builder;
    private NotificationManager nManager;
    private ProgressDialog pd;

    @Override
    public int getLayoutId()
    {
        return R.layout.activity_main;
    }

    @Override
    public void initActionBar()
    {
        super.initActionBar();
    }

    @Override
    public void initData()
    {
        super.initData();
        SharedPreferencesUtil.init(aty, AppContext.getInstance().getUserId() + "TransferRemind", MODE_PRIVATE);
        PermisionUtil.verifyStoragePermissions(this);

        instance = this;
        appUpdater = new AppUpdater(this);
        appUpdater.setOnNewVersionListener(this);
        appUpdater.setOnNoNewVersionListener(this);
        appUpdater.setOnCompleteListener(this);
        appUpdater.setOnProgressListener(this);
        /**
         * 版本正常上线时要打开，必须保证版本之间的迭代
         */
        appUpdater.checkForUpdate();

        mDoubleClickExit = new DoubleClickExitHelper(this);
    }

    /**
     * 监听返回时间，二次点击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 是否退出应用
            if (AppContext.get(Constants.KEY_DOUBLE_CLICK_EXIT, true))
            {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 是否已经下载了最新的APP
     *
     * @param appName app名称
     * @return 返回true表示文件已下载，返回false表示文件没下载
     */
    private boolean hasAlreadyDownloadApp(String appName)
    {
        File file = new File(MainActivity.this.getExternalFilesDir(""), appName);
        return file.exists();
    }

    @Override
    public void onNewVersion(final AppUpdater.UpdateInfo upInfo)
    {
        if (upInfo != null)
        {
            if (this.hasAlreadyDownloadApp(upInfo.getAppName()))
            {
                InformationDialog dialog = new InformationDialog(aty);
                dialog.setTitle("提示");
                dialog.setMessage("输血移动护理已更新到最新版本,请安装!");
                dialog.setEnableClose(false);
                dialog.setPositiveButton("安装", new InformationDialog.IDialogClickListener()
                {
                    @Override
                    public void onDialogClick(Dialog dialog, View view)
                    {
                        dialog.dismiss();
                        Message msg = new Message();
                        msg.what = 101;
                        Bundle bld = new Bundle();
                        bld.putString("appName", upInfo.getAppName());
                        msg.setData(bld);
                        mhandler.sendMessage(msg);
                    }
                });
                if (upInfo.getUseFlag().equalsIgnoreCase("1"))// 当前版本仍可以用
                {
                    dialog.setNegativeButton("稍后再说", new InformationDialog.IDialogClickListener()
                    {
                        @Override
                        public void onDialogClick(Dialog dialog, View view)
                        {
                            dialog.dismiss();
                        }
                    });
                } else
                {
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("退出应用", new InformationDialog.IDialogClickListener()
                    {
                        @Override
                        public void onDialogClick(Dialog dialog, View view)
                        {
                            dialog.dismiss();
                            MainActivity.this.finish();
                        }
                    });
                }
                dialog.show();

                return;
            }

            InformationDialog dialog = new InformationDialog(aty);
            dialog.setTitle("有新版本 " + upInfo.getVersion());
            dialog.setMessage(upInfo.getVersionMemo());
            dialog.setEnableClose(false);
            dialog.setPositiveButton("立即更新", new InformationDialog.IDialogClickListener()
            {
                @Override
                public void onDialogClick(Dialog dialog, View view)
                {
                    dialog.dismiss();
                    if (!SystemUtil.isWiFi(MainActivity.this))// 当前是否是wifi连接
                    {
                        InformationDialog loadDialog = new InformationDialog(aty);
                        loadDialog.setTitle("提示");
                        loadDialog.setMessage("当前非WIFI连接网络,建议切换到WIFI联网下载。确定下载?");
                        loadDialog.setEnableClose(false);
                        loadDialog.setPositiveButton("继续下载", new InformationDialog.IDialogClickListener()
                        {
                            @Override
                            public void onDialogClick(Dialog dialog, View view)
                            {
                                dialog.dismiss();
                                DownloadApp(false, upInfo);
                            }
                        });
                        loadDialog.setNegativeButton("取消下载", new InformationDialog.IDialogClickListener()
                        {
                            @Override
                            public void onDialogClick(Dialog dialog, View view)
                            {
                                dialog.dismiss();
                                if (upInfo.getUseFlag().equalsIgnoreCase("0"))
                                    MainActivity.this.finish();
                            }
                        });
                        loadDialog.show();
                    } else
                    {
                        DownloadApp(false, upInfo);
                    }
                }
            });

            if (upInfo.getUseFlag().equalsIgnoreCase("1"))// 当前版本可以用，可以选择更新或不更新
            {

                /** 1：用户选择立即更新 1.1：检查当前是否是在wifi模式下 1.1.1：当前在wifi模式下，开始在后台更新
                 * 1.1.2：不在wifi模式下 ，询问用户是否继续更新，如果选择了继续更新 ，则进行后台更新，否则取消更新
                 * 2：用户选择稍后更新 2.1：关闭更新消息框，并不能影响当前用户使用*/
                dialog.setNegativeButton("稍后更新", new InformationDialog.IDialogClickListener()
                {
                    @Override
                    public void onDialogClick(Dialog dialog, View view)
                    {
                        dialog.dismiss();
                    }
                });

            } else if (upInfo.getUseFlag().equalsIgnoreCase("0"))// 当前版本不可用了，必须要更新
            {
                dialog.setCancelable(false);
                dialog.setNegativeButton("退出应用", new InformationDialog.IDialogClickListener()
                {
                    @Override
                    public void onDialogClick(Dialog dialog, View view)
                    {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                });
            }

            dialog.show();
        }
    }

    @Override
    public void onNoNewVersion()
    {
        skipActivity(aty, WebViewActivity.class);
    }

    /**
     * 下载APP
     *
     * @param background 是否是后台下载
     * @param upInfo     app信息对象
     */
    @TargetApi(16)
    private void DownloadApp(boolean background, final AppUpdater.UpdateInfo upInfo)
    {
        this.isBackground = background;
        if (this.isBackground)
        {
            nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification = new Notification();
            builder = new Notification.Builder(this);

            builder.setContentText("正在连接，请稍等");
            builder.setContentTitle("升级包下载");
            builder.setSmallIcon(R.mipmap.ic_launcher);

            builder.setAutoCancel(true);
            builder.setWhen(System.currentTimeMillis());
            builder.setNumber(1);

            notification = builder.build();

            try
            {
                nManager.notify(100, notification);
            } catch (Exception ex)
            {
                Log.d("Notification", ex.getMessage());
            }
        } else
        {
            pd = new ProgressDialog(this);
            pd.setTitle("下载");
            pd.setMessage("升级包正在下载中。请稍后!");
            pd.setValueTextType(ProgressDialog.SCALE_DIVIDE, 1024, "KB");

            pd.setProgressValue(0);
            pd.setCancelable(false);
            pd.setButton2("取消下载", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    InformationDialog dialog = new InformationDialog(aty);
                    dialog.setTitle("提示");
                    dialog.setMessage("确定要取消下载?");
                    dialog.setEnableClose(false);
                    dialog.setPositiveButton("是", new InformationDialog.IDialogClickListener()
                    {
                        @Override
                        public void onDialogClick(Dialog dialog, View view)
                        {
                            dialog.dismiss();
                            try
                            {
                                downloadRequest.cancel();//取消下载
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            File file = new File(MainActivity.this.getExternalFilesDir(""), upInfo.getAppName());
                            if (file.exists()) file.delete(); //取消下载要删除文件
                            if (upInfo.getUseFlag().equalsIgnoreCase("0"))
                                MainActivity.this.finish();
                        }
                    });
                    dialog.setNegativeButton("否", new InformationDialog.IDialogClickListener()
                    {
                        @Override
                        public void onDialogClick(Dialog dialog, View view)
                        {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
            pd.show();

        }
        this.appUpdater.downloadApp(upInfo);
    }

    @TargetApi(16)
    @Override
    public void onComplete(AppUpdater.UpdateInfo upInfo)
    {
        if (this.isBackground)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), upInfo.getAppName())), "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentText("升级包已下载完成");
            builder.setContentTitle("点击安装");
            builder.setContentIntent(pendingIntent);
            notification = builder.build();

            nManager.notify(100, notification);
        } else
        {
            Message msg = new Message();
            msg.what = 101;
            Bundle bld = new Bundle();
            bld.putString("appName", upInfo.getAppName());
            msg.setData(bld);
            mhandler.sendMessage(msg);

            pd.dismiss();

            if (upInfo.getUseFlag().equals("0"))
            {
                MainActivity.this.finish();
            }
        }
    }

    @TargetApi(16)
    @Override
    public void onProgress(int fileSize, int downloadSize, DownloadRequest downloadRequest)
    {
        if (this.downloadRequest == null) this.downloadRequest = downloadRequest;
        this.downloadPercent = (int) ((long) downloadSize / (long) fileSize);
        if (this.isBackground)
        {
            builder.setContentText("升级包正在下载中");
            builder.setContentTitle("已下载" + downloadPercent + "%");
            notification = builder.build();
            nManager.notify(100, notification);
        } else
        {
            pd.setProgressMaxValue(fileSize);
            pd.setProgressValue(downloadSize);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }

    private void reSetupApk(Context mContext, String filePath, String fileName)
    {
        Intent intentUpdate = new Intent("android.intent.action.VIEW");
        intentUpdate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(filePath + "/" + fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {  //对Android N及以上的版本做判断
            Uri apkUriN = FileProvider.getUriForFile(mContext,
                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            intentUpdate.addCategory("android.intent.category.DEFAULT");

            intentUpdate.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);   //添加Flag 表示我们需要什么权限

            intentUpdate.setDataAndType(apkUriN, "application/vnd.android.package-archive");

        } else
        {
            Uri apkUri = Uri.fromFile(file);
            intentUpdate.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        mContext.startActivity(intentUpdate);

    }
}