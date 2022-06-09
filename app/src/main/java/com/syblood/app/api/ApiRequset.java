package com.syblood.app.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口访问类
 * <P>
 * Created by xw on 2021-08-03.
 */
public class ApiRequset
{
    /**
     * 首页
     *
     * @param appName 应用名
     * @param version 版本
     */
    public static CommonRequest versionInfo(Object requestSource, String appCode, String appName, String version, ApiRequestCallBack callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("appCode", appCode);
        params.put("appName", appName);
        params.put("version", version);

        return ApiAccessHttp.getRequestInstance().apiAccess(requestSource, "psAppVersionInfo", params, callBack);
    }
}
