package com.syblood.app.api;

import com.syblood.app.constants.Constants;
import com.syblood.app.utils.EncriptUtil;
import com.syblood.app.utils.LogUtil;

import java.util.Map;

/**
 * Api访问请求对象
 * <p>
 * Created by xw on 2016-7-21.
 */
public class ApiRequestObjectBak
{
    /**
     * 业务码
     */
    private String _businessId;

    /**
     * 授权码
     */
    private String _keyCode;

    /**
     * 医院码
     */
    private String _hospitalCode;

    /**
     * 参数列表
     */
    private Map<String, Object> _paramsMap;

    /**
     * 获取业务码
     *
     * @return 业务码
     */
    public String get_businessId()
    {
        return _businessId;
    }

    /**
     * 设置业务码
     *
     * @param _businessId 业务码
     */
    public void set_businessId(String _businessId)
    {
        this._businessId = _businessId;
    }

    /**
     * 获取授权码
     *
     * @return 授权码
     */
    public String get_keyCode()
    {
        return _keyCode;
    }

    /**
     * 设置授权码
     *
     * @param _keyCode 授权码
     */
    public void set_keyCode(String _keyCode)
    {
        this._keyCode = _keyCode;
    }

    /**
     * 获取参数
     *
     * @return 参数Map
     */
    public Map<String, Object> get_paramsMap()
    {
        return _paramsMap;
    }

    /**
     * 设置参数
     *
     * @param _paramsMap 参数
     */
    public void set_paramsMap(Map<String, Object> _paramsMap)
    {
        this._paramsMap = _paramsMap;
    }

    public String get_hospitalCode()
    {
        return _hospitalCode;
    }

    public void set_hospitalCode(String _hospitalCode)
    {
        this._hospitalCode = _hospitalCode;
    }

    /**
     * 转换为XML字符串
     *
     * @param isAES 是否AES加密
     * @return 返回XML格式字符串
     */
    public String toXmlString(boolean isAES)
    {
        StringBuffer strXml = new StringBuffer();

        strXml.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>")
                .append("<request businessId=\"" + this.get_businessId() + "\" keyCode=\"" + this.get_keyCode() + "\" hospitalCode=\"" + this.get_hospitalCode() + "\" >");

        //参数
        if (this.get_paramsMap() != null && !this.get_paramsMap().isEmpty())
        {
            for (Object obj : this.get_paramsMap().keySet())
            {
                strXml.append("<" + String.valueOf(obj) + "><![CDATA[" + String.valueOf(this.get_paramsMap().get(obj)) + "]]></" + String.valueOf(obj) + ">");
            }
        }
        strXml.append("</request>");
        LogUtil.i(strXml.toString());
        if (isAES)
        {
            return EncriptUtil.encryptAES(strXml.toString(), Constants.aesKey, Constants.aesIv);
        } else
        {
            return strXml.toString();
        }
    }

    /**
     * @return
     */
    public String toXmlString()
    {
        return toXmlString(false);
    }

    @Override
    public String toString()
    {
        return "ApiRequestObject{" +
                "_businessId='" + _businessId + '\'' +
                ", _keyCode='" + _keyCode + '\'' +
                ", _paramsMap=" + _paramsMap +
                '}';
    }

    /**
     * 构造函数
     *
     * @param businessId 业务码
     * @param keyCode    授权码
     * @param paramsMap  参数
     */
    public ApiRequestObjectBak(String businessId, String keyCode, Map<String, Object> paramsMap)
    {
        this._businessId = businessId;
        this._keyCode = keyCode;
        this._paramsMap = paramsMap;
        this._hospitalCode = "";
    }

    /**
     * 构造函数
     *
     * @param businessId 业务码
     * @param keyCode    授权码
     * @param paramsMap  参数
     */
    public ApiRequestObjectBak(String businessId, String keyCode, String hospitalCode, Map<String, Object> paramsMap)
    {
        this._businessId = businessId;
        this._keyCode = keyCode;
        this._hospitalCode = hospitalCode;
        this._paramsMap = paramsMap;
    }
}
