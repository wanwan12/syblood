package com.syblood.app.utils;

import android.content.Context;

import com.syblood.app.application.CrashHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Properties工具类
 * <p/>
 * Created by xw on 2016-8-1.
 */
public class PropertiesUtil
{
    /**
     * Context
     */
    private Context mContext;

    /**
     * AppConfig
     */
    private static PropertiesUtil sPropertiesUtil;

    /**
     * 路径名
     */
    private String mPath = "config";

    /**
     * 单例获取
     *
     * @param context Context
     * @param path    存储路径
     * @return
     */
    public static PropertiesUtil getPropertiesUtil(Context context, String path)
    {
        if (sPropertiesUtil == null)
        {
            sPropertiesUtil = new PropertiesUtil();
            sPropertiesUtil.mContext = context;
            sPropertiesUtil.mPath = path;
        }
        return sPropertiesUtil;
    }

    /**
     * 单例获取
     *
     * @param context Context
     * @return
     */
    public static PropertiesUtil getPropertiesUtil(Context context)
    {
        if (sPropertiesUtil == null)
        {
            sPropertiesUtil = new PropertiesUtil();
            sPropertiesUtil.mContext = context;
        }
        return sPropertiesUtil;
    }

    /**
     * 获取key值
     *
     * @param key
     * @return
     */
    public String get(String key)
    {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    /**
     * 获取Properties
     *
     * @return
     */
    public Properties get()
    {
        FileInputStream fis = null;
        Properties props = new Properties();

        try
        {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(mPath, Context.MODE_PRIVATE);
            if (dirConf.exists())
            {
                if (FileUtil.fileIsExists(dirConf.getPath() + File.separator
                        + mPath))
                {
                    fis = new FileInputStream(dirConf.getPath() + File.separator
                            + mPath);
                    props.load(fis);
                }
            }

        } catch (Exception e)
        {
            CrashHandler.getInstance().io(e);
        } finally
        {
            if(fis!=null)
            {
                try
                {
                    fis.close();
                } catch (IOException e)
                {
                    CrashHandler.getInstance().io(e);
                }
            }
        }

        return props;
    }

    /**
     * 设置Properties
     *
     * @param p
     */
    private void setProps(Properties p)
    {
        FileOutputStream fos = null;
        try
        {
            File dirConf = mContext.getDir(mPath, Context.MODE_PRIVATE);
            File conf = new File(dirConf, mPath);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e)
        {
            CrashHandler.getInstance().io(e);
        } finally
        {
            try
            {
                fos.close();
            } catch (IOException e)
            {
                CrashHandler.getInstance().io(e);
            }
        }
    }

    /**
     * 设置Properties
     *
     * @param ps
     */
    public void set(Properties ps)
    {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public void set(String key, String value)
    {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    /**
     * 移除
     *
     * @param key
     */
    public void remove(String... key)
    {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}
