/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/7 0007 23:18:20
 * @version 1.0
 */

package net.xdclass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay.wechat")
public class WeChatPayConfig {
    /**
     * @description TODO 
     * #商户号       
     * @return 
     * @author 
     * @date  
     */
    private String mcId;
    /**
     * @description TODO 
     *     #公众号id 需要和商户号绑定   
     * @return 
     * @author 
     * @date  
     */
    private String WxPayAppid;
    private String mchSerialNo;
    /**
     * @description TODO 
     * #api密钥       
     * @return 
     * @author 
     * @date  
     */
    private String apiV3Key;
    /**
     * @description TODO 
     * #商户私钥路径（微信服务端会根据证书序列号，找到证书获取公钥进行解密数据）       
     * @return 
     * @author 
     * @date  
     */
    private String privateKeyPath;
    /**
     * @description TODO 
     * #支付成功页面跳转       
     * @return 
     * @author 
     * @date  
     */
    private String successReturnUrl;
    /**
     * @description TODO 
     * #支付成功，回调通知       
     * @return 
     * @author 
     * @date  
     */
    private String callbackUrl;
    
    /**
     * @description TODO 
     * 内部使用;
     * @return 
     * @author 
     * @date  
     */

    public static class Url{
        /**
         * @description TODO 
         *    Native下单
         * @return 
         * @author 
         * @date  
         */
        public static final String NATIVE_ORDER="https://api.mch.weixin.qq.com/v3/pay/transactions/native";
        public static final String NATIVE_ORDER_PATH="v3/pay/transactions/native";
/**
 * @description TODO
 * native关闭订单接口；
 * @return
 * @author
 * @date
 */
        public static final String NATIVE_CLOSE=" https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s?mchid=%s/close";
        public static final String NATIVE_CLOSE_PATH=" v3/pay/transactions/out-trade-no/%s?mchid=%s/close";



    }

}


