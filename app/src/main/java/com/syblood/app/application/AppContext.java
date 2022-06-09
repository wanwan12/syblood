package com.syblood.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.syblood.app.BuildConfig;
import com.syblood.app.api.ApiAccessHttp;
import com.syblood.app.constants.Constants;
import com.syblood.app.constants.Keys;
import com.syblood.app.models.UserInfo;
import com.syblood.app.utils.DataCleanUtil;
import com.syblood.app.utils.LogUtil;
import com.syblood.app.utils.MethodsCompatUtil;
import com.syblood.app.utils.PropertiesUtil;
import com.syblood.app.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import androidx.multidex.MultiDex;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * <p/>
 * Created by xw on 2016-7-29.
 */
public class AppContext extends BaseApplication {
    /**
     * 单例
     */
    private static AppContext SInstance;

    /**
     * 当前用户ID
     */
    private String mUserId;

    /**
     * 是否登录
     */
    private boolean mIsLogin;

    /**
     * 个推CID
     */
    private String cid = "";

//    private RefWatcher mRefWatcher;

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static synchronized AppContext getInstance() {
        return SInstance;
    }

    /**
     * 获取是否首次
     *
     * @return
     */
    public static boolean isFristStart() {
        return getPreferences().getBoolean(Keys.PrefeKey.KEY_FRITST_START, true);
    }

    /**
     * 设置是否首次
     *
     * @param frist
     */
    public static void setFristStart(boolean frist) {
        set(Keys.PrefeKey.KEY_FRITST_START, frist);
    }

    /**
     * 个推CID
     *
     * @return
     */
    public String getCid() {
        return cid;
    }

    /**
     * 个推CID
     *
     * @param cid
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * 本地存储-个推CID
     */
    public String getLocCid() {
        return get("cid", "");
    }

    /**
     * 本地存储-个推CID
     *
     * @param cid
     */
    public void setLocCid(String cid) {
        set("cid", cid);
    }

    /**
     * 本地存储WebViewUrl
     */
    public String getWebViewUrl() {
        return get("WebViewUrl", "");
    }

    /**
     * 本地存储WebViewUrl
     *
     * @param webViewUrl
     */
    public void setWebViewUrl(String webViewUrl) {
        set("WebViewUrl", webViewUrl);
    }

    /**
     * 本地存储-是否在线
     */
    public String getIsOnline() {
        return get("isOnline", "");
    }

    /**
     * 本地存储-是否在线
     *
     * @param isOnline
     */
    public void setIsOnline(String isOnline) {
        set("isOnline", isOnline);
    }

    /**
     * 本地存储-当前定位区域
     */
    public String getLocAreaCode() {
        return get("locAreaCode", "3601");
    }

    /**
     * 本地存储-当前定位区域
     *
     * @param areaCode
     */
    public void setLocAreaCode(String areaCode) {
        set("locAreaCode", areaCode);
    }

    /**
     * 本地存储-当前定位区域
     */
    public String getLocAreaName() {
        return get("locAreaName", "江西省 南昌市");
    }

    /**
     * 本地存储-当前定位区域
     *
     * @param areaName
     */
    public void setLocAreaName(String areaName) {
        set("locAreaName", areaName);
    }

    /**
     * 本地存储-当前定位区域
     */
    public String getLocDistrictAreaCode() {
        return get("locDistrictAreaCode", "360111");
    }

    /**
     * 本地存储-当前定位区域
     *
     * @param areaCode
     */
    public void setLocDistrictAreaCode(String areaCode) {
        set("locDistrictAreaCode", areaCode);
    }

    /**
     * 本地存储-当前定位区域
     */
    public String getLocDistrictAreaName() {
        return get("locDistrictAreaName", "南昌市 青山湖区");
    }

    /**
     * 本地存储-当前定位区域
     *
     * @param areaName
     */
    public void setLocDistrictAreaName(String areaName) {
        set("locDistrictAreaName", areaName);
    }

    /**
     * 标记定位是否成功     0失败1成功
     *
     * @return
     */
    public String getLocFlag() {
        return get("locFlag", "1");
    }

    /**
     * 标记定位是否成功     0失败1成功
     *
     * @param flag
     */
    public void setLocFlag(String flag) {
        set("locFlag", flag);
    }

    /**
     * 标记是否显示转门诊说明
     *
     * @return
     */
    public boolean getIsFristTransferClinic() {
        return get("isFristTransferClinic", false);
    }

    /**
     * 标记是否显示转门诊说明
     *
     * @param isFrist
     */
    public void setIsFristTransferClinic(boolean isFrist) {
        set("isFristTransferClinic", isFrist);
    }

    /**
    * Activity列表
    * */
    private List<Activity> mActivityList = new ArrayList<>();

    @Override
    public void onCreate() {
        /**内存泄漏检测*/
//        mRefWatcher = LeakCanary.install(this);

        Constants.KEY_FLAG = "D";

        super.onCreate();
        SInstance = this;

        initLog();
        initHttp();
        initImgLoader();
        initPushService();

        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mActivityList.add(activity);
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivityList.remove(activity);
            }
        });
    }

    public Activity getActivity(Class<?> cls)
    {
        Activity activity = null;
        for (Activity aty : mActivityList)
        {
            if (aty.getClass().equals(cls))
            {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    /**
     * 初始化Log控制器
     */
    protected void initLog() {
        LogUtil.setDebug(BuildConfig.DEBUG);
        LogUtil.setTag("SYBLOOD");
    }

    /**
     * 初始化HTTP请求
     */
    private void initHttp() {
        ApiAccessHttp.getRequestInstance().setHttpURL(Constants.API_ADDRESS);
        ApiAccessHttp.getRequestInstance().setDebugHttp(BuildConfig.DEBUG);
        ApiAccessHttp.getRequestInstance().setDefConnectTimeout(60 * 1000);
        ApiAccessHttp.getRequestInstance().setDefReadTimeout(60 * 1000);
        ApiAccessHttp.getRequestInstance().setKeyCode(Constants.KEY_CODE);
    }

    /**
     * 初始化登录
     */
    protected void initLogin() {
        UserInfo user = getLoginUser();
        if (null != user && !StringUtil.isEmpty(user.getUserId())) {
            mUserId = user.getUserId();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 初始化图片加载
     */
    protected void initImgLoader() {

    }

    /**
     * 初始化推送相关
     */
    protected void initPushService() {

    }

    /**
     * 保存登录信息
     *
     * @param user     用户信息
     * @param isUpdate 是否更新
     */
    public void saveUserInfo(final UserInfo user, boolean isUpdate) {
        this.mUserId = user.getUserId();
        this.mIsLogin = true;
        setProperties(new Properties() {
            {
                setProperty("user.doctorId", String.valueOf(user.getUserId()));
                setProperty("user.mobileNo", user.getMobileNo());
                if (!StringUtil.isEmpty(user.getPassword())) {
                    setProperty("user.password", user.getPassword());
                }
                setProperty("user.registerDate", user.getRegisterDate());
                setProperty("user.cidOn", user.getCidOn());
                setProperty("user.onLine", user.getOnLine());
                setProperty("user.realName", user.getRealName());
                setProperty("user.headPic", user.getHeadPic());
                setProperty("user.idNumber", user.getIdNumber());
                setProperty("user.hospitalCode", user.getHospitalCode());
                setProperty("user.hospitalName", user.getHospitalName());
                setProperty("user.officeCode", user.getOfficeCode());
                setProperty("user.officeName", user.getOfficeName());
                setProperty("user.titleCode", user.getTitleCode());
                setProperty("user.titleName", user.getTitleName());
                setProperty("user.goodAt", user.getGoodAt());
                setProperty("user.introduction", user.getIntroduction());
                setProperty("user.doctorPic", user.getDoctorPic());
            }
        });
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    public void saveUserInfo(final UserInfo user) {
        saveUserInfo(user, false);
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    public void updateUserInfo(final UserInfo user) {
        saveUserInfo(user, true);
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public UserInfo getLoginUser() {
        UserInfo user = new UserInfo();

        user.setUserId(getProperty("user.userId"));
        user.setMobileNo(getProperty("user.mobileNo"));
        user.setPassword(getProperty("user.password"));
        user.setRegisterDate(getProperty("user.registerDate"));
        user.setCidOn(getProperty("user.cidOn"));
        user.setOnLine(getProperty("user.onLine"));
        user.setRealName(getProperty("user.realName"));
        user.setHeadPic(getProperty("user.headPic"));
        user.setIdNumber(getProperty("user.idNumber"));
        user.setHospitalCode(getProperty("user.hospitalCode"));
        user.setHospitalName(getProperty("user.hospitalName"));
        user.setOfficeCode(getProperty("user.officeCode"));
        user.setOfficeName(getProperty("user.officeName"));
        user.setTitleCode(getProperty("user.titleCode"));
        user.setTitleName(getProperty("user.titleName"));
        user.setGoodAt(getProperty("user.goodAt"));
        user.setIntroduction(getProperty("user.introduction"));
        user.setDoctorPic(getProperty("user.doctorPic"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.mUserId = "";
        this.mIsLogin = false;
        removeProperty("user.userId", "user.mobileNo", "user.password", "user.registerDate", "user.cidOn",
                "user.onLine", "user.authenticateState", "user.realName", "user.headPic", "user.idNumber",
                "user.hospitalCode", "user.hospitalName", "user.officeCode", "user.officeName", "user.titleCode",
                "user.titleName", "user.goodAt", "user.introduction", "user.doctorPic", "user.accountBalance",
                "user.integralBalance", "user.answerNumber", "user.textNumber", "user.telephoneNumber", "user.videoNumber",
                "user.clinicNumber", "user.evaluationNumber", "user.evaluation1Number", "user.evaluation2Number", "user.evaluation3Number",
                "user.recommendNumber", "user.followerNumber", "user.contractNumber", "user.acceptTreatment", "user.acceptExaminate",
                "user.acceptText", "user.acceptTelephone", "user.acceptVideo", "user.textPrice", "user.telephonePrice",
                "user.videoPrice", "user.telephoneDate", "user.videoDate", "user.acceptClinic", "user.clinicPrice",
                "user.clinicDate", "user.clinicAddress", "user.volunteText", "user.volunteTextNumber", "user.volunteTextSurplus",
                "user.volunteTextDate", "user.volunteTelephone", "user.volunteTelephoneNumber", "user.volunteTelephoneSurplus", "user.volunteTelephoneDate",
                "user.familyDoctor", "user.platform", "user.area_code", "user.area_name", "user.referMemo", "user.transferMemo", "user.acceptEmergency");
    }

    /**
     * 获取医生Id
     *
     * @return
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return mIsLogin;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        this.mIsLogin = false;
        this.mUserId = "";
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        // 清除数据缓存
        DataCleanUtil.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (MethodsCompatUtil.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanUtil.cleanCustomCache(MethodsCompatUtil.getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }

    /**
     * 是否包含 Properties key
     *
     * @param key KEY
     * @return
     */
    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    /**
     * 获取Properties
     *
     * @return
     */
    public Properties getProperties() {
        return PropertiesUtil.getPropertiesUtil(this).get();
    }

    /**
     * 设置Properties
     *
     * @param ps
     */
    public void setProperties(Properties ps) {
        PropertiesUtil.getPropertiesUtil(this).set(ps);
    }

    /**
     * 设置 Property
     *
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        PropertiesUtil.getPropertiesUtil(this).set(key, value);
    }

    /**
     * 获取属性
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = PropertiesUtil.getPropertiesUtil(this).get(key);
        return res;
    }

    /**
     * 移除 Property
     *
     * @param key
     */
    public void removeProperty(String... key) {
        PropertiesUtil.getPropertiesUtil(this).remove(key);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
