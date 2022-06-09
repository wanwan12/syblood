package com.syblood.app.constants;

/**
 * KEY键值
 * <p/>
 * Created by xw on 2016-7-29.
 */
public class Keys
{
    /**
     * SharedPreferences KEY键值
     */
    public class PrefeKey
    {
        /**
         * 首次进入
         */
        public static final String KEY_FRITST_START = "KEY_FRIST_START";
    }

    /**
     * 跳转到支付界面时，给支付界面传订单信息对象的key值
     */
    public final static String PAY_ORDER_INFO = "orderInfo";

    /**
     * 用于页面判断是否是从支付页面跳转过来的
     */
    public final static String PAY_IS_FORWARD ="isPayForward";

}
