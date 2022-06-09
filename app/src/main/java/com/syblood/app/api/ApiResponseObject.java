package com.syblood.app.api;

import com.syblood.app.utils.GsonUtil;
import com.syblood.app.utils.ObjectUtil;
import com.syblood.app.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Api访问返回对象
 * <p/>
 * Created by xw on 2016-7-21.
 */
public class ApiResponseObject
{
    /**
     * 返回结果
     */
    private String _returnCode;

    /**
     * 返回信息
     */
    private String _returnMsg;

    /**
     * 返回的结果集合
     */
    private Map<String, Object> _resultMap;

    /**
     * 缓存结果集合
     */
    private Map<String, Object> _cacheMap;

    /**
     * 获取返回码
     *
     * @return
     */
    public String get_returnCode()
    {
        return _returnCode;
    }

    /**
     * 设置返回码
     *
     * @param _returnCode
     */
    public void set_returnCode(String _returnCode)
    {
        this._returnCode = _returnCode;
    }

    /**
     * 获取返回码
     *
     * @return
     */
    public boolean isSuccess()
    {
        return get_returnCode().equals("success");
    }

    /**
     * 获取返回信息
     *
     * @return
     */
    public String get_returnMsg()
    {
        return _returnMsg;
    }

    /**
     * 设置返回信息
     *
     * @param _returnMsg
     */
    public void set_returnMsg(String _returnMsg)
    {
        this._returnMsg = _returnMsg;
    }


    /**
     * 获取结果Map
     *
     * @return
     */
    public Map<String, Object> get_resultMap()
    {
        return _resultMap;
    }

    /**
     * 设置结果
     *
     * @param _resultMap 结果Map
     */
    public void set_resultMap(Map<String, Object> _resultMap)
    {
        this._resultMap = _resultMap;
    }

    /**
     * 设置缓存结果集
     *
     * @return
     */
    public Map<String, Object> get_cacheMap()
    {
        return _cacheMap;
    }

    /**
     * 设置缓存结果集
     *
     * @param _cacheMap 结果Map
     */
    public void set_cacheMap(Map<String, Object> _cacheMap)
    {
        this._cacheMap = _cacheMap;
    }

    /**
     * 获取Map值
     *
     * @param key key
     * @return
     */
    public String get_MapValue(Object key)
    {
        if (this.get_resultMap().containsKey(key))
        {
            if (get_resultMap().get(key) == null)
            {
                return "";
            }
            return this.get_resultMap().get(key).toString();
        }
        return "";
    }

    /**
     * json转实体类
     *
     * @param key Map Key  返回xml数据节点
     * @return
     */
    public JSONObject JsonToObject(Object key)
    {
        JSONObject jObj = null;
        if (get_MapValue(key) == null || get_MapValue(key).equals("[]") || StringUtil.isEmpty(get_MapValue(key)))
        {
            return jObj;
        }
        try
        {
            JSONArray jArr = new JSONArray(get_MapValue(key));
            jObj = jArr.getJSONObject(0);
        } catch (JSONException e)
        {
            e.printStackTrace();
        } finally
        {
            return jObj;
        }
    }

    /**
     * json转实体类
     *
     * @param key
     * @param cls
     * @param <T>
     */
    public <T> T JsonToCls(Object key, Class<T[]> cls)
    {
        if (get_MapValue(key) == null || get_MapValue(key).equals("[]") || StringUtil.isEmpty(get_MapValue(key)))
        {
            return null;
        }
        return GsonUtil.toCls(get_MapValue(key), cls);
    }

    /**
     * json转实体类
     *
     * @param key
     * @param cls
     * @param <T>
     */
    public <T> List<T> JsonToList(Object key, Class<T[]> cls)
    {
        if (get_MapValue(key) == null || get_MapValue(key).equals("[]") || StringUtil.isEmpty(get_MapValue(key)))
        {
            return new ArrayList<T>();
        }
        return GsonUtil.toClsList(get_MapValue(key), cls);
    }

    /**
     * 构造函数 解析返回值
     */
    public ApiResponseObject(String responseJson)
    {
        if (responseJson.equals("WEBSERVICE_ERROR"))
        {
            this._returnCode = "fail";
            this._returnMsg = "网络异常";
            return;
        }

        this._resultMap = new HashMap<String, Object>();
        try
        {
            //JSONArray jArr = new JSONArray(responseJson);
            //JSONObject jObj = jArr.getJSONObject(0);

            JSONObject jObj = new JSONObject(responseJson);
            //判断接口请求返回的错误信息
            this._returnCode = jObj.getString("code");
            this._returnMsg = jObj.getString("message");
            //处理缓存数据
            if (ObjectUtil.isNotEmpty(jObj.get("cache")))
            {
                JSONObject object = jObj.getJSONObject("cache");
                Iterator it = jObj.keys();
                while (it.hasNext())
                {
                    String key = (String) it.next();
                    Object value = jObj.get(key);
                    this._cacheMap.put(key, value);
                }
            }

            if (_returnCode.equals("success"))
            {
                if (!jObj.isNull("result"))
                {
                    JSONObject object = jObj.getJSONObject("result");
                    Iterator it = object.keys();
                    while (it.hasNext())
                    {
                        String key = (String) it.next();
                        String value = object.getString(key);
                        this._resultMap.put(key, value);
                    }
                }
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            this._returnCode = "fail";
            this._returnMsg = "服务器繁忙";
        }
    }

    /**
     * 构造函数 解析返回值
     */
    /*public ApiResponseObject(String responseXml)
    {
        if (responseXml.equals("WEBSERVICE_ERROR"))
        {
            this._returnCode = "fail";
            this._returnMsg = "网络异常";
            return;
        }

        this._resultMap = new HashMap<String, Object>();
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (responseXml.indexOf("<paramInfo>") > 0)
            {
                responseXml = responseXml.replace("<paramInfo>", "<paramInfo><![CDATA[");
                responseXml = responseXml.replace("</paramInfo>", "]]></paramInfo>");
            }
            Document doc = builder.parse(new InputSource(new StringReader(
                    responseXml)));

            Element root = doc.getDocumentElement();
            this._returnCode = root.getAttribute("returnCode");
            this._returnMsg = root.getAttribute("returnMsg");
            String format = root.getAttribute("type");
            if (!StringUtil.isEmpty(format) && format.equals("json"))
            {
                String data = root.getFirstChild().getNodeValue();
                if (!StringUtil.isEmpty(data))
                {
                    try
                    {
                        JSONArray jArr = new JSONArray(data);
                        JSONObject jObj = jArr.getJSONObject(0);

                        Iterator it = jObj.keys();
                        while (it.hasNext())
                        {
                            String key = (String) it.next();
                            String value = jObj.getString(key);
                            this._resultMap.put(key, value);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            } else
            {
                NodeList childNodes = root.getChildNodes();
                if (childNodes != null)
                {
                    for (int i = 0; i < childNodes.getLength(); i++)
                    {
                        Node node = childNodes.item(i);
                        this._resultMap.put(node.getNodeName(), (node.getFirstChild() != null ? node.getFirstChild().getNodeValue() : ""));
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            this._returnCode = "fail";
            this._returnMsg = "服务器繁忙";
        }
    }*/
}
