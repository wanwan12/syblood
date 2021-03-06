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
     * ??????????????????
     */
    private boolean isBackground = false;
    /**
     * ?????????????????????
     */
    private long downloadPercent = 0;
    /**
     * ??????????????????????????????
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
         * ??????????????????????????????????????????????????????????????????
         */
        appUpdater.checkForUpdate();

        mDoubleClickExit = new DoubleClickExitHelper(this);
    }

    /**
     * ???????????????????????????????????????
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
            // ??????????????????
            if (AppContext.get(Constants.KEY_DOUBLE_CLICK_EXIT, true))
            {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * ??????????????????????????????APP
     *
     * @param appName app??????
     * @return ??????true??????????????????????????????false?????????????????????
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
                dialog.setTitle("??????");
                dialog.setMessage("??????????????????????????????????????????,?????????!");
                dialog.setEnableClose(false);
                dialog.setPositiveButton("??????", new InformationDialog.IDialogClickListener()
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
                if (upInfo.getUseFlag().equalsIgnoreCase("1"))// ????????????????????????
                {
                    dialog.setNegativeButton("????????????", new InformationDialog.IDialogClickListener()
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
                    dialog.setNegativeButton("????????????", new InformationDialog.IDialogClickListener()
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
            dialog.setTitle("???????????? " + upInfo.getVersion());
            dialog.setMessage(upInfo.getVersionMemo());
            dialog.setEnableClose(false);
            dialog.setPositiveButton("????????????", new InformationDialog.IDialogClickListener()
            {
                @Override
                public void onDialogClick(Dialog dialog, View view)
                {
                    dialog.dismiss();
                    if (!SystemUtil.isWiFi(MainActivity.this))// ???????????????wifi??????
                    {
                        InformationDialog loadDialog = new InformationDialog(aty);
                        loadDialog.setTitle("??????");
                        loadDialog.setMessage("?????????WIFI????????????,???????????????WIFI????????????????????????????");
                        loadDialog.setEnableClose(false);
                        loadDialog.setPositiveButton("????????????", new InformationDialog.IDialogClickListener()
                        {
                            @Override
                            public void onDialogClick(Dialog dialog, View view)
                            {
                                dialog.dismiss();
                                DownloadApp(false, upInfo);
                            }
                        });
                        loadDialog.setNegativeButton("????????????", new InformationDialog.IDialogClickListener()
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

            if (upInfo.getUseFlag().equalsIgnoreCase("1"))// ??????????????????????????????????????????????????????
            {

                /** 1??????????????????????????? 1.1???????????????????????????wifi????????? 1.1.1????????????wifi?????????????????????????????????
                 * 1.1.2?????????wifi????????? ??????????????????????????????????????????????????????????????? ?????????????????????????????????????????????
                 * 2??????????????????????????? 2.1????????????????????????????????????????????????????????????*/
                dialog.setNegativeButton("????????????", new InformationDialog.IDialogClickListener()
                {
                    @Override
                    public void onDialogClick(Dialog dialog, View view)
                    {
                        dialog.dismiss();
                    }
                });

            } else if (upInfo.getUseFlag().equalsIgnoreCase("0"))// ??????????????????????????????????????????
            {
                dialog.setCancelable(false);
                dialog.setNegativeButton("????????????", new InformationDialog.IDialogClickListener()
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
     * ??????APP
     *
     * @param background ?????????????????????
     * @param upInfo     app????????????
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

            builder.setContentText("????????????????????????");
            builder.setContentTitle("???????????????");
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
            pd.setTitle("??????");
            pd.setMessage("????????????????????????????????????!");
            pd.setValueTextType(ProgressDialog.SCALE_DIVIDE, 1024, "KB");

            pd.setProgressValue(0);
            pd.setCancelable(false);
            pd.setButton2("????????????", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    InformationDialog dialog = new InformationDialog(aty);
                    dialog.setTitle("??????");
                    dialog.setMessage("??????????????????????");
                    dialog.setEnableClose(false);
                    dialog.setPositiveButton("???", new InformationDialog.IDialogClickListener()
                    {
                        @Override
                        public void onDialogClick(Dialog dialog, View view)
                        {
                            dialog.dismiss();
                            try
                            {
                                downloadRequest.cancel();//????????????
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            File file = new File(MainActivity.this.getExternalFilesDir(""), upInfo.getAppName());
                            if (file.exists()) file.delete(); //???????????????????????????
                            if (upInfo.getUseFlag().equalsIgnoreCase("0"))
                                MainActivity.this.finish();
                        }
                    });
                    dialog.setNegativeButton("???", new InformationDialog.IDialogClickListener()
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
            builder.setContentText("????????????????????????");
            builder.setContentTitle("????????????");
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
            builder.setContentText("????????????????????????");
            builder.setContentTitle("?????????" + downloadPercent + "%");
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
        {  //???Android N???????????????????????????
            Uri apkUriN = FileProvider.getUriForFile(mContext,
                    mContext.getApplicationContext().getPackageName() + ".provider", file);
            intentUpdate.addCategory("android.intent.category.DEFAULT");

            intentUpdate.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);   //??????Flag ??????????????????????????????

            intentUpdate.setDataAndType(apkUriN, "application/vnd.android.package-archive");

        } else
        {
            Uri apkUri = Uri.fromFile(file);
            intentUpdate.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        mContext.startActivity(intentUpdate);

    }
}