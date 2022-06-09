package com.syblood.app.api;

import com.syblood.app.constants.Constants;
import com.syblood.app.utils.Base64Util;
import com.syblood.app.utils.EncriptUtil;
import com.syblood.app.utils.GsonUtil;
import com.syblood.app.utils.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Api访问请求对象
 * <p>
 * Created by xw on 2021-8-22.
 */
public class ApiRequestObject
{
    /**
     * 业务码
     */
    private String _apiCode;

    /**
     * 授权码
     */
    private String _sid;

    /**
     * 系统编号
     */
    private String _subsysNo;

    /**
     * 参数列表
     */
    private Map<String, Object> _param;

    /**
     * 格式方式
     */
    private String _format;

    /**
     * 格式方式
     */
    private boolean _isUserCache;

    /**
     * 时间戳
     */
    private String _timestamp;

    /**
     * 获取业务码
     *
     * @return 业务码
     */
    public String get_apiCode()
    {
        return _apiCode;
    }

    /**
     * 设置业务码
     *
     * @param _apiCode 业务码
     */
    public void set_apiCode(String _apiCode)
    {
        this._apiCode = _apiCode;
    }

    /**
     * 获取授权码
     *
     * @return 授权码
     */
    public String get_sid()
    {
        return _sid;
    }

    /**
     * 设置授权码
     *
     * @param _sid 授权码
     */
    public void set_sid(String _sid)
    {
        this._sid = _sid;
    }

    /**
     * 获取系统编号
     *
     * @return 系统编号
     */
    public String get_subsysNo()
    {
        return _subsysNo;
    }

    /**
     * 设置系统编号
     *
     * @param _subsysNo 系统编号
     */
    public void set_subsysNo(String _subsysNo)
    {
        this._subsysNo = _subsysNo;
    }

    /**
     * 获取系统编号
     *
     * @return 系统编号
     */
    public String get_timestamp()
    {
        return _timestamp;
    }

    /**
     * 设置时间戳
     *
     * @param _timestamp 时间戳
     */
    public void set_timestamp(String _timestamp)
    {
        this._timestamp = _timestamp;
    }



    /**
     * 获取参数
     *
     * @return 参数Map
     */
    public Map<String, Object> get_param()
    {
        return _param;
    }

    /**
     * 获取参数-转换为字符串
     *
     * @return 参数Map
     */
    public String get_paramString(boolean isEncode, String encodeType)
    {
        String paramStr = "";

        //参数
        if (this.get_param() != null && !this.get_param().isEmpty())
        {
            paramStr = GsonUtil.toJson(this.get_param());
            if(isEncode)
            {
                if(encodeType.equals("Base64"))
                {
                    try
                    {
                        paramStr = Base64Util.encode(paramStr.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e)
                    {
                        LogUtil.e("参数Base64编码错误");
                        e.printStackTrace();
                    }
                }
            }
        }

        return paramStr;
    }

    /**
     * 设置参数
     *
     * @param _param 参数
     */
    public void set_param(Map<String, Object> _param)
    {
        this._param = _param;
    }

    /**
     * 获取数据格式化类型
     *
     * @return 数据格式化类型
     */
    public String get_format()
    {
        return _format;
    }


    /**
     * 设置数据格式化类型
     *
     * @param _format 数据格式化类型
     */
    public void set_format(String _format)
    {
        this._format = _format;
    }

    /**
     * 获取数据格式化类型
     *
     * @return 数据格式化类型
     */
    public boolean get_isUserCache()
    {
        return _isUserCache;
    }

    /**
     * 设置是否返回用户缓存
     *
     * @param _isUserCache 是否返回用户缓存
     */
    public void set_isUserCache(boolean _isUserCache)
    {
        this._isUserCache = _isUserCache;
    }

    /**
     * 转换为JSON字符串
     *
     * @param isEncode   是否参数编码
     * @param encodeType 参数编码方式
     * @return
     */
    public String toJsonString(boolean isEncode, String encodeType)
    {
        StringBuffer strJson = new StringBuffer();

        strJson.append("{")
                .append("\"sid\":\""+this.get_sid()+"\",")
                .append("\"format\":\""+this.get_format()+"\",")
                .append("\"subsysNo\":\""+this.get_subsysNo()+"\",")
                .append("\"timestamp\":\""+this.get_timestamp()+"\",")
                .append("\"apiCode\":\""+this.get_apiCode()+"\",")
                .append("\"isUserCache\":"+String.valueOf(this.get_isUserCache())+",")
                .append("\"param\":\""+ this.get_paramString(isEncode, encodeType)+"\"")
                .append("}");

        return strJson.toString();
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
                .append("<request apiCode=\"" + this.get_apiCode() + "\" sid=\"" + this.get_sid() + "\" subsysNo=\"" + this.get_subsysNo() + "\" >");

        //参数
        if (this.get_param() != null && !this.get_param().isEmpty())
        {
            for (Object obj : this.get_param().keySet())
            {
                strXml.append("<" + String.valueOf(obj) + "><![CDATA[" + String.valueOf(this.get_param().get(obj)) + "]]></" + String.valueOf(obj) + ">");
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
                "_apiCode='" + _apiCode + '\'' +
                ", _sid='" + _sid + '\'' +
                ", _param=" + _param +
                '}';
    }

    /**
     * 构造函数
     *
     * @param apiCode 业务码
     * @param sid     授权码
     * @param param   参数
     */
    public ApiRequestObject(String apiCode, String sid, Map<String, Object> param)
    {
        this._apiCode = apiCode;
        this._sid = sid;
        this._param = param;
        this._subsysNo = "";
        this._timestamp = "";
    }

    /**
     * 构造函数
     *
     * @param apiCode 业务码
     * @param sid     授权码
     * @param param   参数
     */
    public ApiRequestObject(String apiCode, String sid, String subsysNo, Map<String, Object> param)
    {
        this._apiCode = apiCode;
        this._sid = sid;
        this._subsysNo = subsysNo;
        this._param = param;
    }
}
