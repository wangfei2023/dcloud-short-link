/**
 * @project dcloud-short-link
 * @description 对接微信
 * @author Administrator
 * @date 2023/7/9 0009 10:57:15
 * @version 1.0
 */

package net.xdclass.component;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.WeChatPayConfig;
import net.xdclass.config.WechatPayApi;
import net.xdclass.vo.PayInfoVO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class WechatPayStrategy  implements PayStrategy{


    @Autowired
    private WeChatPayConfig payConfig ;

    @Autowired
    private CloseableHttpClient wechatPayClient ;

    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        //过期时间  RFC 3339格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        //支付订单过期时间
        String timeExpire = sdf.format(new Date(System.currentTimeMillis() + payInfoVO.getOrderPayTimeoutMills()));

        JSONObject amountObj = new JSONObject();
        //数据库存储是double比如，100.99元，微信支付需要以分为单位
        int amount = payInfoVO.getPayFee().multiply(BigDecimal.valueOf(100)).intValue();
        amountObj.put("total", amount);
        amountObj.put("currency", "CNY");

        JSONObject payObj = new JSONObject();
        payObj.put("mchid", payConfig.getMchId());
        payObj.put("out_trade_no", payInfoVO.getOutTradeNo());
        payObj.put("appid", payConfig.getWxPayAppid());
        payObj.put("description", payInfoVO.getTitle());
        payObj.put("notify_url", payConfig.getCallbackUrl());

        payObj.put("time_expire", timeExpire);
        payObj.put("amount", amountObj);
        //回调携带
        payObj.put("attach", "{\"accountNo\":" + payInfoVO.getAccountNo() + "}");


        // 处理请求body参数
        String body = payObj.toJSONString();

        log.info("请求参数:{}", body);

        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");

        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);

        String result = "";

        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost)) {

            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());

            log.info("微信支付响应:resp code={}，return body={}", statusCode, responseStr);
            //处理成功
            if (statusCode == HttpStatus.OK.value()) {
                JSONObject jsonObject = JSONObject.parseObject(responseStr);
                if (jsonObject.containsKey("code_url")) {
                    result = jsonObject.getString("code_url");
                }
            } else {
                log.error("微信支付响应失败:resp code={}，return body={}", statusCode, responseStr);
            }

        } catch (Exception e) {
            log.error("微信支付响应异常信息:{}", e);

        }
        return result;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        return null;
    }
}


