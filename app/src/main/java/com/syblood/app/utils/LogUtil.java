package com.syblood.app.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 日志工具类
 * <p>
 * Created by xw on 2016-7-28.
 */
public final class LogUtil
{
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    /**
     * 最小堆栈跟踪索引
     */
    private static final int MIN_STACK_OFFSET = 2;

    /**
     * Json格式缩进
     */
    private static final int JSON_INDENT = 2;

    /**
     * 最大块长度
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * 日志Tag
     */
    private static String STag = "YLWJ";
    /**
     * 日志调试
     */
    private static boolean SDebug = true;

    /**
     * 显示方法数
     */
    private static int SMethodCount = 2;

    /**
     * 日志调试
     */
    private static boolean SIsShowThreadInfo = true;

    /**
     * 最小堆栈跟踪索引
     */
    private static int SMethodOffset = 0;

    /**
     * 显示Activity状态
     */
    private static boolean SActivityState = true;

    /**
     * 初始化
     *
     * @param debug            是否调试
     * @param tag              Tag
     * @param methodCount      方法数
     * @param isShowThreadInfo 是否显示线程信息
     */
    public static void init(boolean debug, String tag, int methodCount, boolean isShowThreadInfo, int methodOffset)
    {
        STag = tag;
        SDebug = debug;
        SMethodCount = methodCount;
        SIsShowThreadInfo = isShowThreadInfo;
        SMethodOffset = methodOffset;
    }

    /**
     * 初始化
     */
    public static void init(boolean debug, String tag)
    {
        SDebug = debug;
        STag = tag;
    }

    /**
     * 设置Tag标记
     *
     * @param tag tag.
     */
    public static void setTag(String tag)
    {
        STag = tag;
    }

    /**
     * 打开调试模式
     *
     * @param debug true 打开, false 关闭.
     */
    public static void setDebug(boolean debug)
    {
        SDebug = debug;
    }

    /**
     * 显示Activity状态
     *
     * @param debug true 打开, false 关闭.
     */
    public static void setDebugActivityState(boolean debug)
    {
        SActivityState = debug;
    }


    /**
     * DEBUG
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void d(String message, Object... args)
    {
        log(DEBUG, null, message, args);
    }

    /**
     * DEBUG
     *
     * @param object 日志内容
     */
    public static void d(Object object)
    {
        String message;
        if (object.getClass().isArray())
        {
            message = Arrays.deepToString((Object[]) object);
        } else
        {
            message = object.toString();
        }
        log(DEBUG, null, message);
    }

    /**
     * ERROR
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void e(String message, Object... args)
    {
        e(null, message, args);
    }

    /**
     * ERROR
     *
     * @param throwable 异常
     * @param message   日志模板
     * @param args      参数
     */
    public static void e(Throwable throwable, String message, Object... args)
    {
        log(ERROR, throwable, message, args);
    }

    /**
     * WARN
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void w(String message, Object... args)
    {
        log(WARN, null, message, args);
    }

    /**
     * INFO
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void i(String message, Object... args)
    {
        log(INFO, null, message, args);
    }

    /**
     * VERBOSE
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void v(String message, Object... args)
    {
        log(VERBOSE, null, message, args);
    }

    /**
     * ASSERT
     *
     * @param message 日志模板
     * @param args    参数
     */
    public static void wtf(String message, Object... args)
    {
        log(ASSERT, null, message, args);
    }

    /**
     * Json日志
     *
     * @param json Json内容
     */
    public static void json(String json)
    {
        if (StringUtil.isEmpty(json))
        {
            d("Empty/Null json content");
            return;
        }
        try
        {
            json = json.trim();
            if (json.startsWith("{"))
            {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("["))
            {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e)
        {
            e("Invalid Json");
        }
    }

    /**
     * Xml日志
     *
     * @param xml the xml content
     */
    public static void xml(String xml)
    {
        if (StringUtil.isEmpty(xml))
        {
            d("Empty/Null xml content");
            return;
        }
        try
        {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e)
        {
            e("Invalid xml");
        }
    }

    /**
     * 显示状态
     *
     * @param packName 报名
     * @param state    状态
     */
    public static void state(String packName, String state)
    {
        if (SActivityState)
        {
            log(DEBUG, STag + "-activity_state", packName + state, null);
        }
    }

    /**
     * 日志
     *
     * @param priority  日记类型(LogUtil.DEBUG)
     * @param tag       Tag
     * @param message   消息
     * @param throwable 异常
     */
    public static synchronized void log(int priority, String tag, String message, Throwable throwable)
    {
        if (!SDebug)
        {
            return;
        }
        if (throwable != null && message != null)
        {
            message += " : " + SystemUtil.getStackTraceString(throwable);
        }
        if (throwable != null && message == null)
        {
            message = SystemUtil.getStackTraceString(throwable);
        }
        if (message == null)
        {
            message = "No message/exception is set";
        }
        if (StringUtil.isEmpty(message))
        {
            message = "Empty/NULL log message";
        }

        logTopBorder(priority, tag);
        logHeaderContent(priority, tag, SMethodCount);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE)
        {
            if (SMethodCount > 0)
            {
                logDivider(priority, tag);
            }
            logContent(priority, tag, message);
            logBottomBorder(priority, tag);
            return;
        }
        if (SMethodCount > 0)
        {
            logDivider(priority, tag);
        }
        for (int i = 0; i < length; i += CHUNK_SIZE)
        {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, tag, new String(bytes, i, count));
        }
        logBottomBorder(priority, tag);
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private static synchronized void log(int priority, Throwable throwable, String msg, Object... args)
    {
        if (!SDebug)
        {
            return;
        }
        String tag = STag;
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable);
    }

    /**
     * 头部框
     *
     * @param logType 日志类型
     * @param tag     Tag
     */
    private static void logTopBorder(int logType, String tag)
    {
        logChunk(logType, tag, TOP_BORDER);
    }

    /**
     * 日志头部内容
     *
     * @param logType     日志类型
     * @param tag         Tag
     * @param methodCount 方法数
     */
    private static void logHeaderContent(int logType, String tag, int methodCount)
    {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (SIsShowThreadInfo)
        {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";

        int stackOffset = getStackOffset(trace) + SMethodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length)
        {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--)
        {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length)
            {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("║ ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            logChunk(logType, tag, builder.toString());
        }
    }

    /**
     * 日志内容
     *
     * @param logType 日志类型
     * @param tag     Tag
     * @param chunk   内容块
     */
    private static void logContent(int logType, String tag, String chunk)
    {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines)
        {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
        }
    }

    /**
     * 日志底部框
     *
     * @param logType 日志类型
     * @param tag     Tag
     */
    private static void logBottomBorder(int logType, String tag)
    {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    /**
     * 日志输出
     *
     * @param logType 日志类型
     * @param tag     Tag
     * @param chunk   内容块
     */
    private static void logChunk(int logType, String tag, String chunk)
    {
        String finalTag = formatTag(tag);
        switch (logType)
        {
            case ERROR:
                Log.e(finalTag, chunk);
                break;
            case INFO:
                Log.i(finalTag, chunk);
                break;
            case VERBOSE:
                Log.v(finalTag, chunk);
                break;
            case WARN:
                Log.w(finalTag, chunk);
                break;
            case ASSERT:
                Log.wtf(finalTag, chunk);
                break;
            case DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(finalTag, chunk);
                break;
        }
    }

    /**
     * 格式化Tag
     *
     * @param tag
     * @return
     */
    private static String formatTag(String tag)
    {
        if (!StringUtil.isEmpty(tag) && !StringUtil.equals(STag, tag))
        {
            return STag + "-" + tag;
        }
        return STag;
    }

    /**
     * 创建消息 通过格式解析 %S
     *
     * @param message 消息模板
     * @param args    参数
     * @return
     */
    private static String createMessage(String message, Object... args)
    {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }

    /**
     * 获取简单的ClassName
     *
     * @param name ClassName
     * @return
     */
    private static String getSimpleClassName(String name)
    {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * 分割线
     *
     * @param logType
     * @param tag
     */
    private static void logDivider(int logType, String tag)
    {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    /**
     * 获取堆栈的开始索引
     *
     * @param trace 堆栈跟踪
     * @return 堆栈偏移量
     */
    private static int getStackOffset(StackTraceElement[] trace)
    {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++)
        {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LogUtil.class.getName()))
            {
                return --i;
            }
        }
        return -1;
    }
}
