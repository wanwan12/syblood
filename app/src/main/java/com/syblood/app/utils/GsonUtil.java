package com.syblood.app.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Gson解析Json工具类
 * <p/>
 * Created by xw on 2021-7-21.
 */
public class GsonUtil
{
    /**
     * JsonString转Class对象
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T toClsObj(String jsonString, Class<T> cls)
    {
        T t = null;
        Gson gson = new Gson();

        t = gson.fromJson(jsonString, cls);

        return t;
    }

    /**
     * JsonString转Class对象
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T toCls(String jsonString, Class<T[]> cls)
    {
        List<T> list = new ArrayList<>();

        try
        {
            Gson gson = new Gson();
            list = Arrays.asList(gson.fromJson(jsonString, cls));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (list != null)
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * JsonString转List<T>
     *
     * @param jsonString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toClsList(String jsonString, Class<T[]> cls)
    {
        List<T> list = new ArrayList<>();
        if (StringUtil.isEmpty(jsonString))
        {
            return list;
        }
        try
        {
            Gson gson = new Gson();
            List<T> _list = Arrays.asList(gson.fromJson(jsonString, cls));
            if (_list != null && _list.size() > 0)
            {
                list.addAll(_list);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * jsonString转List<String>
     *
     * @param jsonString
     * @return
     */
    public static List<String> toStrings(String jsonString)
    {
        List<String> list = new ArrayList<>();
        try
        {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<String>>()
            {
            }.getType());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 实体类转json
     *
     * @param t   实体类对象
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t, Class<T> cls)
    {
        Gson gson = new Gson();
        String jsonText = "";
        jsonText = gson.toJson(t, cls);
        return jsonText;
    }

    /**
     * 实体类转json
     *
     * @param t   实体类对象
     * @param <T>
     * @return
     */
    public static <T> String toJson(T t)
    {
        Gson gson = new Gson();
        String jsonText = "";
        jsonText = gson.toJson(t);
        return jsonText;
    }


    /**
     * jsonString转List<Map>
     *
     * @param jsonString
     * @param cls        Map 值类型Cls
     * @param <T>        Map 值类型Cls
     * @return
     */
    public static <T> List<HashMap<String, T>> toMaps(String jsonString, Class<T> cls)
    {
        List<HashMap<String, T>> list = new ArrayList<HashMap<String, T>>();
        try
        {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<HashMap<String, T>>>()
            {
            }.getType());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
