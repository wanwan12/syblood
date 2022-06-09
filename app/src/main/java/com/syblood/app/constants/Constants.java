package com.syblood.app.constants;

import com.syblood.app.BuildConfig;

/**
 * 常量类，存放接口地址等
 * Created by xw on 2016-6-20.
 */
public final class Constants
{
    /**
     * APP标识
     */
    public static String KEY_FLAG = "";

    public static String APP_CODE = "SYBLOOD-NURSE";

    public static String APP_NAME = "输血移动护理";

    public static final String KEY_DOUBLE_CLICK_EXIT = "KEY_DOUBLE_CLICK_EXIT";

    /**
     * 网络访问 授权码
     */
    public final static String KEY_CODE = "8e0aa5a4b7b2b358b88ad510536583d5";

    /**
     *API地址
     */
    //public final static String API_ADDRESS = WEBSERVER_ADDRESS() + "/appRequestOpera";
    public final static String API_ADDRESS = WEBSERVER_ADDRESS()+"/appRequestOpera";
    /**
     * AES密钥
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    public final static String aesKey = "smkldospdosldaaa";

    public static String WEBSERVICE_ADDRESS = WEBSERVER_ADDRESS();

    /**
     * 协议文件地址
     */
    //public final static String PROTOCOL_ADDRESS = "http://119.3.147.93:8080/RXLMServer/";
    public final static String PROTOCOL_ADDRESS = "http://192.168.3.23:8899/";

    /**
     * AES向量
     */
    public final static String aesIv = "0392039203920300";

    /**
     * webservice接口地址
     */
    public final static String WEBSERVER_ADDRESS() {
        if (BuildConfig.DEBUG) {
            return "http://192.168.3.38:8090/Api";
            //return "http://172.20.10.3:8080/Api";
        } else {
            return "http://192.168.3.38:8090/Api";
            //return "http://172.20.10.3:8080/Api";
        }
    }

    /**
     * 设置接口地址
     *
     * @param webserviceAddress
     */
    public static void setWebserviceAddress(String webserviceAddress)
    {
        if(webserviceAddress != null)
        {
            WEBSERVICE_ADDRESS = webserviceAddress;
        }
    }

    /**
     * 得到接口地址
     *
     * @return
     */
    public static String getWebserviceAddress()
    {
        return WEBSERVICE_ADDRESS;
    }

    public static String getFileBasePath()
    {
        return getWebserviceAddress() + "files/";
    }

}
