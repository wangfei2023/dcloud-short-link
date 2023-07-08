/**
 * @project dcloud-short-link
 * @description 快速验证参数配置方法和统一下单接口开发
 * @author Administrator
 * @date 2023/7/8 0008 21:38:07
 * @version 1.0
 */

package net.xdclass.config;

public class WechatPayApi {

    /**
     * 微信支付域名
     */
    public static final String HOST = "https://api.mch.weixin.qq.com";


    /**
     * Native下单
     */
    public static final String NATIVE_ORDER = HOST+"/v3/pay/transactions/native";
}


