package com.syblood.app.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.syblood.app.R;
import com.syblood.app.utils.LogUtil;
import com.syblood.app.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 * <p>
 * Created by xw on 2016-7-29.
 */
public class CrashHandler implements UncaughtExceptionHandler
{
    public static final String TAG = "CrashHandler";

    /**
     * 定义异常类型
     */
    public final static byte TYPE_NETWORK = 0x01;
    public final static byte TYPE_SOCKET = 0x02;
    public final static byte TYPE_HTTP_CODE = 0x03;
    public final static byte TYPE_HTTP_ERROR = 0x04;
    public final static byte TYPE_XML = 0x05;
    public final static byte TYPE_IO = 0x06;
    public final static byte TYPE_RUN = 0x07;
    public final static byte TYPE_JSON = 0x08;
    public final static byte TYPE_FILENOTFOUND = 0x09;

    /**
     * CrashHandler 实例
     */
    private static CrashHandler INSTANCE = new CrashHandler();

    /**
     * 程序的 Context 对象
     */
    private Context mContext;

    /**
     * 重新启动的页面
     */
    private Class<?> mStartActivity;

    /**
     * 系统默认的 UncaughtException 处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();

    /**
     * 用于格式化日期,作为日志文件名的一部分
     */
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");


    /**
     * 异常的类型
     */
    private static byte type;
    /**
     * 异常的状态码，这里一般是网络请求的状态码
     */
    private static int code;

    public int getCode()
    {
        return this.code;
    }

    public int getType()
    {
        return this.type;
    }

    /**
     * 保证只有一个 CrashHandler 实例
     */
    private CrashHandler()
    {
    }

    /**
     * 获取 CrashHandler 实例 ,单例模式
     */
    public static CrashHandler getInstance()
    {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, Class<?> startActivity)
    {
        mContext = context;
        mStartActivity = startActivity;

        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    public void init(Context context)
    {
        mContext = context;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * http异常
     *
     * @param code 代码
     */
    public void http(int code)
    {
        handleOtherException(TYPE_HTTP_CODE, code, null);
    }

    /**
     * http异常
     *
     * @param code 代码
     * @param e    Exception
     */
    public void http(int code, Exception e)
    {
        handleOtherException(TYPE_HTTP_CODE, code, e);
    }

    /**
     * socket
     *
     * @param code 错误码
     * @param e    Exception
     */
    public void socket(int code, Exception e)
    {
        handleOtherException(TYPE_SOCKET, code, e);
    }

    /**
     * file
     *
     * @param code 错误码
     * @param e    Exception
     */
    public void file(int code, Exception e)
    {
        handleOtherException(TYPE_FILENOTFOUND, code, e);
    }

    /**
     * io
     *
     * @param e Exception
     */
    public void io(Exception e)
    {
        io(0, e);
    }

    /**
     * io
     *
     * @param code 错误码
     * @param e    Exception
     */
    public void io(int code, Exception e)
    {
        if (e instanceof UnknownHostException || e instanceof ConnectException)
        {
            handleOtherException(TYPE_NETWORK, code, e);
        } else if (e instanceof IOException)
        {
            handleOtherException(TYPE_IO, code, e);
        }
        run(code, e);
    }

    public void xml(int code, Exception e)
    {
        handleOtherException(TYPE_XML, code, e);
    }

    public void json(int code, Exception e)
    {
        handleOtherException(TYPE_JSON, code, e);
    }

    /**
     * 网络请求异常
     *
     * @param e
     * @return
     */
    public void network(Exception e, int code)
    {
        if (e instanceof UnknownHostException || e instanceof ConnectException)
        {
            handleOtherException(TYPE_NETWORK, code, e);
        } else if (e instanceof HttpException)
        {
            http(code, e);
        } else if (e instanceof SocketException)
        {
            socket(code, e);
        }
        http(code, e);
    }

    public void run(int code, Exception e)
    {
        handleOtherException(TYPE_RUN, code, e);
    }

    public void run(Exception e)
    {
        run(0, e);
    }


    /**
     * 当 UncaughtException 发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else
        {
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
                LogUtil.e(e, "uncaughtException-%s", e.getMessage());
            }

            // 退出程序,注释下面的重启启动程序代码
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            // 重新启动程序，注释上面的退出程序
            /*Intent intent = new Intent();
            intent.setClass(mContext, mStartActivity);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);*/
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    /**
     * 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成
     *
     * @param ex
     * @return true：如果处理了该异常信息；否则返回 false
     */
    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }

        // 使用 Toast 来显示异常信息
        new Thread()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                ToastUtil.show(mContext, R.string.crash_warn_message);
                Looper.loop();
            }
        }.start();

        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    private boolean handleOtherException(byte type, int code, Exception ex)
    {
        if (ex == null)
        {
            return false;
        }
        this.type = type;
        this.code = code;
        // 使用 Toast 来显示异常信息
        new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				ToastUtil.show(mContext, "很抱歉，程序出现异常。");
				Looper.loop();
			}
		}.start();
        LogUtil.e(ex, "未捕获异常");

        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }


    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);

            if (pi != null)
            {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            LogUtil.e(e, "an error occured when collect package info...-%s", e.getMessage());
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());

                LogUtil.d("an error occured  when collect crash info...", field.getName() + " : " + field.get(null));
            } catch (Exception e)
            {
                LogUtil.e("an error occured  when collect crash info...", e.getMessage());
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex)
    {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null)
        {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append(result);
        try
        {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                String path = "/sdcard/Ylwj/crash/";
                File dir = new File(path);
                if (!dir.exists())
                {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e)
        {
            LogUtil.e("an error occured while writing file...-%s", e.getMessage());
        }

        return null;
    }
}

