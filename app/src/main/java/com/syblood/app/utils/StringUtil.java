package com.syblood.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 字符串操作，以及类型转换等相关方法
 * Created by CJJ on 2016/7/21.
 */
public class StringUtil
{
    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input 字符串
     * @return
     */
    public static boolean isEmpty(CharSequence input)
    {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param strs
     * @return
     */
    public static boolean isEmpty(CharSequence... strs)
    {
        for (CharSequence str : strs)
        {
            if (isEmpty(str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回当前系统时间
     *
     * @param format 时间格式例如：yyyy-mm-dd
     * @return
     */
    public static String getDataTime(String format)
    {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 字符串转整数
     *
     * @param str      待处理字符串
     * @param defValue 默认值
     * @return
     */
    public static int toInt(String str, int defValue)
    {
        try
        {
            return Integer.parseInt(str);
        } catch (Exception e)
        {

        }
        return defValue;
    }

    /**
     * 对象转整
     *
     * @param obj 待处理对象
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj)
    {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     *
     * @param str 待处理字符串
     * @return 转换异常返回 0
     */
    public static long toLong(String str)
    {
        try
        {
            return Long.parseLong(str);
        } catch (Exception e)
        {
        }
        return 0;
    }

    /**
     * String转double
     *
     * @param str 待处理字符串
     * @return 转换异常返回 0
     */
    public static double toDouble(String str)
    {
        try
        {
            return Double.parseDouble(str);
        } catch (Exception e)
        {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     *
     * @param str 待处理字符串
     * @return 转换异常返回 false
     */
    public static boolean toBool(String str)
    {
        try
        {
            return Boolean.parseBoolean(str);
        } catch (Exception e)
        {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str)
    {
        try
        {
            Integer.parseInt(str.toString());
        } catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     *
     * @param data 要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data)
    {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data)
        {
            int v = b & 0xff;
            if (v < 16)
            {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>()
    {
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate 日期字符串
     * @return
     */
    public static Date toDate(String sdate)
    {
        return toDate(sdate, dateFormater.get());
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate        日期字符串
     * @param dateFormater 格式（例：yyyy-mm-dd）
     * @return
     */
    public static Date toDate(String sdate, SimpleDateFormat dateFormater)
    {
        try
        {
            return dateFormater.parse(sdate);
        } catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     * <p/>
     * NOTE: Logic slightly change due to strict policy on CI -
     * "Inner assignments should be avoided"
     */
    public static boolean equals(CharSequence a, CharSequence b)
    {
        if (a == b) return true;
        if (a != null && b != null)
        {
            int length = a.length();
            if (length == b.length())
            {
                if (a instanceof String && b instanceof String)
                {
                    return a.equals(b);
                } else
                {
                    for (int i = 0; i < length; i++)
                    {
                        if (a.charAt(i) != b.charAt(i)) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
