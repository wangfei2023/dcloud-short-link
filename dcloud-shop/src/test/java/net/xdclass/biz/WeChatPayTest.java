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
import org.apache.http.client.methods.HttpGet;
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

    /**
     * 快速验证统一下单接口
     * @throws IOException
     */
    @Test
    public void testNativeOrder() throws IOException {

        String outTradeNo = CommonUtil.getStringNumRandom(32);

        /**
         * {
         * 	"mchid": "1900006XXX",
         * 	"out_trade_no": "native12177525012014070332333",
         * 	"appid": "wxdace645e0bc2cXXX",
         * 	"description": "Image形象店-深圳腾大-QQ公仔",
         * 	"notify_url": "https://weixin.qq.com/",
         * 	"amount": {
         * 		"total": 1,
         * 		"currency": "CNY"
         *        }
         * }
         */
        JSONObject payObj = new JSONObject();
        payObj.put("mchid",payConfig.getMchId());
        payObj.put("out_trade_no",outTradeNo);
        payObj.put("appid",payConfig.getWxPayAppid());
        payObj.put("description","老王和冰冰的红包");
        payObj.put("notify_url",payConfig.getCallbackUrl());

        //订单总金额，单位为分。
        JSONObject amountObj = new JSONObject();
        amountObj.put("total",100);
        amountObj.put("currency","CNY");

        payObj.put("amount",amountObj);
        //附属参数，可以用在回调
        payObj.put("attach","{\"accountNo\":"+888+"}");


        String body = payObj.toJSONString();

        log.info("请求参数:{}",body);

        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/json");

        HttpPost httpPost = new HttpPost(WechatPayApi.NATIVE_ORDER);
        httpPost.setHeader("Accept","application/json");
        httpPost.setEntity(entity);

        try(CloseableHttpResponse response = wechatPayClient.execute(httpPost)){

            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());

            log.info("下单响应码:{},响应体:{}",statusCode,responseStr);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

/**
 * @description TODO 
 *  根据商户订单号查询订单支付状态;
 * @return 
 * @author 
 * @date  
 */
    @Test
    public void testWechatPayNativeQuery() throws IOException {

        String outTradeNo = "mq6rkKGYCwJfy4NKeIxo";
        String url = String.format(WechatPayApi.NATIVE_QUERY, outTradeNo, payConfig.getMchId());

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //httpClient自动进行参数签名
        try (CloseableHttpResponse response = wechatPayClient.execute(httpGet)) {
            String responseStr = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("查询订单响应码={}，响应体={}", statusCode, responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWechatPayNativeCloseOrder() throws IOException {

        String outTradeNo = "ckj99CYGsNwOFAbOzrFTtqB8TDp0O6Fr";
        String url = String.format(WechatPayApi.NATIVE_CLOSE_ORDER, outTradeNo, payConfig.getMchId());

        HttpPost httpPost = new HttpPost(url);

        //组装json
        JSONObject payObj = new JSONObject();

        payObj.put("mchid", payConfig.getMchId());
        String body = payObj.toJSONString();

        log.info("请求参数={}", body);

        //将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(body, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //httpClient自动进行参数签名
        try (CloseableHttpResponse response = wechatPayClient.execute(httpPost);) {
            int statusCode = response.getStatusLine().getStatusCode();//响应状态码
            log.info("关闭订单响应码={}，无响应体", statusCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * @description TODO 
 *  申请订单退款
 * @return 
 * @author 
 * @date  
 */
    /**
     * {"amount":{"currency":"CNY","discount_refund":0,"from":[],"payer_refund":10,
     * "payer_total":100,"refund":10,"settlement_refund":10,"settlement_total":100,"total":100},
     * "channel":"ORIGINAL","create_time":"2022-01-18T13:14:46+08:00",
     * "funds_account":"AVAILABLE","out_refund_no":"Pe9rWbRpUDu51PFvo8L17LJZHm6dpbj7",
     * "out_trade_no":"6xYsHV3UziDINu06B0XeuzmNvOedjhY5","promotion_detail":[],
     * "refund_id":"50302000542022011816569235991","status":"PROCESSING",
     * "transaction_id":"4200001390202201189710793189",
     * "user_received_account":"民生银行信用卡5022"}
     *
     * @throws IOException
     */
    @Test
    public void testNativeRefundOrder() throws IOException {

        String outTradeNo = "mq6rkKGYCwJfy4NKeIxo7z8y84RNpM3B";

        String refundNo = CommonUtil.getStringNumRandom(32);
//        //调用统一下单API
//        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";
      //  HttpPost httpPost = new HttpPost(url);

        // 请求body参数
        JSONObject refundObj = new JSONObject();
        //订单号
        refundObj.put("out_trade_no", outTradeNo);
        //退款单编号，商户系统内部的退款单号，商户系统内部唯一，
        // 只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔
        refundObj.put("out_refund_no", refundNo);
        refundObj.put("reason","商品已售完");
        refundObj.put("notify_url", payConfig.getCallbackUrl());

        JSONObject amountObj = new JSONObject();
        amountObj.put("refund", 100);
        amountObj.put("total", 100);
        amountObj.put("currency", "CNY");

        refundObj.put("amount", amountObj);

        String body = refundObj.toJSONString();
        log.info("请求参数:{}",body);

        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/json");
        HttpPost httpPost=new HttpPost(WechatPayApi.NATIVE_REFUSE_ORDER);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setEntity(entity);

        try(CloseableHttpResponse response = wechatPayClient.execute(httpPost)){
            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());

            log.info("退款响应码:{},响应体:{}",statusCode,responseStr);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    /**
     * {"amount":{"currency":"CNY","discount_refund":0,"from":[],"payer_refund":10,
     * "payer_total":100,"refund":10,"settlement_refund":10,
     * "settlement_total":100,"total":100},"channel":"ORIGINAL",
     * "create_time":"2022-01-18T13:14:46+08:00","funds_account":"AVAILABLE",
     * "out_refund_no":"Pe9rWbRpUDu51PFvo8L17LJZHm6dpbj7",
     * "out_trade_no":"6xYsHV3UziDINu06B0XeuzmNvOedjhY5","promotion_detail":[],
     * "refund_id":"50302000542022011816569235991","status":"SUCCESS",
     * "success_time":"2022-01-18T13:14:55+08:00","transaction_id":"4200001390202201189710793189",
     * "user_received_account":"民生银行信用卡5022"}
     * @throws IOException
     */
    /**
     * @description TODO 
     *     -订单退款状态查询
     * @return 
     * @author 
     * @date  
     */
    @Test
    public void testNativeRefundQuery() throws IOException {


        String refundNo = "ZhG8JkUtMIgRazU50JqgbYOLNG9Aheru";

        String url = String.format(WechatPayApi.NATIVE_REFUSE_STATE_QUERY, refundNo);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        try (CloseableHttpResponse response = wechatPayClient.execute(httpGet)) {

            //响应码
            int statusCode = response.getStatusLine().getStatusCode();
            //响应体
            String responseStr = EntityUtils.toString(response.getEntity());

            log.info("查询退款响应码:{},响应体:{}", statusCode, responseStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


