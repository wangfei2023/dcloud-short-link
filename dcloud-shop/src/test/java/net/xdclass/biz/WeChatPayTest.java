 /**
 * @project dcloud-short-link
 * @description流量包订单测试类;
 * @author Administrator
 * @date 2023/7/4 0004 00:00:04
 * @version 1.0
 */

package net.xdclass.biz;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShopApplication;
import net.xdclass.config.PayBeanConfig;
import net.xdclass.config.WeChatPayConfig;
import net.xdclass.config.WechatPayApi;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.utils.CommonUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@Slf4j
public class WeChatPayTest {
    @Autowired
    private PayBeanConfig payBeanConfig ;

    @Autowired
    private WeChatPayConfig payConfig ;

    @Autowired
    private CloseableHttpClient wechatPayClient ;
    @Test
    public void testLoadPrivatKey() throws Exception{
        log.info(payBeanConfig.getPrivateKey().getAlgorithm());
    }
    @Test
    public void testWechatPayNativeOrder() throws IOException {

        //过期时间  RFC 3339格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        //支付订单过期时间
        String timeExpire = sdf.format(new Date(System.currentTimeMillis() + 6000 * 60 * 1000));
        String outTradeNo = CommonUtil.getStringNumRandom(32);

        JSONObject amountObj = new JSONObject();
        JSONObject payObj = new JSONObject();
        payObj.put("mchid", payConfig.getMchId());
        payObj.put("out_trade_no", outTradeNo);
        payObj.put("appid", payConfig.getWxPayAppid());
        payObj.put("description", "爱上一只灰色的大灰狼");
        payObj.put("notify_url", payConfig.getCallbackUrl());
        payObj.put("time_expire", timeExpire);

        //微信支付需要以分为单位
        int amount = 100;
        amountObj.put("total", amount);
        amountObj.put("currency", "CNY");
        payObj.put("amount", amountObj);

        //附属参数，可以用在回调携带
        payObj.put("attach", "{\"accountNo\":" + 8888 + "}");

        // 处理请求body参数
        String body = payObj.toJSONString();
        log.info("请求参数:{}", payObj);
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");

        //调用统一下单API
        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);

        //httpClient自动进行参数签名
        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost)) {

            String responseStr = EntityUtils.toString(response.getEntity());
            //响应体
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("统一下单响应码={}，响应体={}", statusCode, responseStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


