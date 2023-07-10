/**
 * @project dcloud-short-link
 * @description 微信支付回调通知
 * @author Administrator
 * @date 2023/7/10 0010 09:54:22
 * @version 1.0
 */

package net.xdclass.controller.request;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.WeChatPayConfig;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.service.ProductOrderService;
import net.xdclass.utils.JsonData;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("api/callback/order/v1")
@Slf4j
public class PayCallBackController {
    @Autowired
    private WeChatPayConfig weChatPayConfig;

    @Autowired
    private ProductOrderService  productOrderService;

    @Autowired
    private ScheduledUpdateCertificatesVerifier verifier;
    @RequestMapping("wechat")
    @ResponseBody
    public Map<String,String>wecatPayCallBack(HttpServletRequest request, HttpServletResponse response){
        //获取报文（以流的形式读取;）
       String body = getRequestBody(request);
        //随机串
        String nonceStr = request.getHeader("Wechatpay-Nonce");

        //微信传递过来的签名 m
        String signature = request.getHeader("Wechatpay-Signature");

        //证书序列号（微信平台）
        String serialNo = request.getHeader("Wechatpay-Serial");

        //时间戳
        String timestamp = request.getHeader("Wechatpay-Timestamp");

        //构造验签名串
//  包含一下几点
//        应答时间戳\n
//        应答随机串\n
//        应答报文主体\n
        String signStr = Stream.of(timestamp, nonceStr, body).collect(Collectors.joining("\n", "", "\n"));
        HashMap<String, String> map = new HashMap<>(2);
        try {
            boolean result = verifiedSign(serialNo, signStr, signature);
            if (result){
                //解密数据;
                String plainBody = decryptBody(body);
                log.info("解密后的明文：{}",plainBody);
                Map<String, String> paramMap = covertWechatPayMsgToMap(plainBody);
            //处理业务逻辑;TODO：
                JsonData jsonData = productOrderService.processOrderCallBackMsg(ProductOrderPayTypeEnum.WECHAT_PAY, paramMap);
//            响应微信
            map.put("code","SUCCESS");
            map.put("message","成功");
            }
        } catch (Exception e) {
            log.error("微信支付回调异常",e);
        }
        return map;
    }

    /**
     * @description TODO
     * 转换boby为map;
     *
     * 商户对resource对象进行解密后，
     *
     * {
     *     "transaction_id":"1217752501201407033233368018",
     *     "amount":{
     *         "payer_total":100,
     *         "total":100,
     *         "currency":"CNY",
     *         "payer_currency":"CNY"
     *     },
     *     "mchid":"1230000109",
     *     "trade_state":"SUCCESS",
     *     "bank_type":"CMC",
     *     "promotion_detail":[
     *         {
     *             "amount":100,
     *             "wechatpay_contribute":0,
     *             "coupon_id":"109519",
     *             "scope":"GLOBAL",
     *             "merchant_contribute":0,
     *             "name":"单品惠-6",
     *             "other_contribute":0,
     *             "currency":"CNY",
     *             "stock_id":"931386",
     *             "goods_detail":[
     *                 {
     *                     "goods_remark":"商品备注信息",
     *                     "quantity":1,
     *                     "discount_amount":1,
     *                     "goods_id":"M1006",
     *                     "unit_price":100
     *                 },
     *                 {
     *                     "goods_remark":"商品备注信息",
     *                     "quantity":1,
     *                     "discount_amount":1,
     *                     "goods_id":"M1006",
     *                     "unit_price":100
     *                 }
     *             ]
     *         },
     *         {
     *             "amount":100,
     *             "wechatpay_contribute":0,
     *             "coupon_id":"109519",
     *             "scope":"GLOBAL",
     *             "merchant_contribute":0,
     *             "name":"单品惠-6",
     *             "other_contribute":0,
     *             "currency":"CNY",
     *             "stock_id":"931386",
     *             "goods_detail":[
     *                 {
     *                     "goods_remark":"商品备注信息",
     *                     "quantity":1,
     *                     "discount_amount":1,
     *                     "goods_id":"M1006",
     *                     "unit_price":100
     *                 },
     *                 {
     *                     "goods_remark":"商品备注信息",
     *                     "quantity":1,
     *                     "discount_amount":1,
     *                     "goods_id":"M1006",
     *                     "unit_price":100
     *                 }
     *             ]
     *         }
     *     ],
     *     "success_time":"2018-06-08T10:34:56+08:00",
     *     "payer":{
     *         "openid":"oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
     *     },
     *     "out_trade_no":"1217752501201407033233368018",
     *     "appid":"wxd678efh567hg6787",
     *     "trade_state_desc":"支付成功",
     *     "trade_type":"MICROPAY",
     *     "attach":"自定义数据",
     *     "scene_info":{
     *         "device_id":"013467007045764"
     *     }
     * }
     * @return
     * @author
     * @date
     */
    private Map<String, String> covertWechatPayMsgToMap(String plainBody){
        HashMap<String, String> paramMap = new HashMap<>(2);
        JSONObject object = JSONObject.parseObject(plainBody);
        //订单
        paramMap.put("out_trade_no", object.getString("out_trade_no"));
        //交易状态
        paramMap.put("trade_state",  object.getString("trade_state"));

        paramMap.put("account_no",  object.getJSONObject("attach").getString("accountNo"));
        return paramMap;
    }


    /**
     *
     *
     * @description TODO
     * 解密body 密文
     *
     * 支付通知回调；
     * {
     *     "id": "EV-2018022511223320873",
     *     "create_time": "2015-05-20T13:29:35+08:00",
     *     "resource_type": "encrypt-resource",
     *     "event_type": "TRANSACTION.SUCCESS",
     *     "summary": "支付成功",
     *     "resource": {
     *         "original_type": "transaction",
     *         "algorithm": "AEAD_AES_256_GCM",
     *         "ciphertext": "",
     *         "associated_data": "",
     *         "nonce": ""
     *     }
     * }
     * @return
     * @author
     * @date
     */

    private String decryptBody(String body) throws UnsupportedEncodingException, GeneralSecurityException {
        AesUtil aesUtil=  new AesUtil(weChatPayConfig.getApiV3Key().getBytes("utf-8")) ;
        JSONObject object = JSONObject.parseObject(body);
        JSONObject resource = object.getJSONObject("resource");
        String ciphertext = resource.getString("ciphertext");
        String associatedData = resource.getString("associated_data");
        String nonce = resource.getString("nonce");
        //进行解密;
        String decrypt = aesUtil.decryptToString(associatedData.getBytes("utf-8"), nonce.getBytes("utf-8"), ciphertext);

      return decrypt;

    }
    /**
     * 验证签名
     *
     * @param serialNo  微信平台-证书序列号
     * @param signStr   自己组装的签名串
     * @param signature 微信返回的签名
     * @return
     * @throws UnsupportedEncodingException
     */
    private boolean verifiedSign(String serialNo, String signStr, String signature) throws UnsupportedEncodingException {
        return verifier.verify(serialNo, signStr.getBytes("utf-8"), signature);
    }

    private String  getRequestBody(HttpServletRequest request){
        StringBuffer stringBuffer = new StringBuffer();//比SttingBuild效率高;
        try {
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!=null){
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }
}


