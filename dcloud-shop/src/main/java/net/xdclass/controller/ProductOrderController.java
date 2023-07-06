package net.xdclass.controller;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.annoatation.RepeatSubmit;
import net.xdclass.constant.RedisKey;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.controller.request.ProductOrderPageRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.ClientTypeEnum;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.service.ProductOrderService;
import net.xdclass.service.ProductService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MrWang
 * @since 2023-07-03
 */
@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {
    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;
   /**
    * @description TODO
    * 获取token;
    * @return
    * @author
    * @date
    */
    @GetMapping("token")
    public JsonData getToken(){
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String token = CommonUtil.getStringNumRandom(32);
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY, accountNo, token);
        redisTemplate.opsForValue().set(key,String.valueOf(Thread.currentThread().getId()),30, TimeUnit.MINUTES);
         return JsonData.buildSuccess(token);
    }
    /**
     * @description TODO 
     * 分业接口;       
     * @return 
     * @author 
     * @date  
     */
//   @GetMapping("page")
//   @RepeatSubmit(limitType =RepeatSubmit.Type.TOKEN )
//    public JsonData pageResult(
//            @RequestParam(value = "page", defaultValue = "1")int page,
//            @RequestParam(value = "size", defaultValue = "10")int size,
//            @RequestParam(value = "status", required = false)String  status
//   ){
//       Map<String,Object> pageResult= productOrderService.page(page,size,status);
//       return JsonData.buildSuccess(pageResult);
//   }


    @PostMapping("page")
    @RepeatSubmit(limitType =RepeatSubmit.Type.TOKEN )
    public JsonData pageResult(@RequestBody ProductOrderPageRequest productOrderPageRequest){
        Map<String,Object> pageResult= productOrderService.page(productOrderPageRequest);
        return JsonData.buildSuccess(pageResult);
    }
   
   /**
    * @description TODO 
    * 查询订单的状态;
    * @return 
    * @author 
    * @date  
    */
   @GetMapping("query_status")
   public JsonData queryStatus(@RequestParam("out_trade_no")String outTradeNo ){
       String status =   productOrderService.queryProductOrder(outTradeNo);
      return StringUtils.isEmpty(status)?JsonData.buildResult(BizCodeEnum.ORDER_CONFIRM_NOT_EXIST):JsonData.buildSuccess();
   }
  /**
   * @description TODO
   * 下单接口;
   * @return
   * @author
   * @date
   */
    @GetMapping("confirm")
    public void OrderConfirm(@RequestBody ConfirmOrderRequest confirmOrderRequest, HttpServletResponse httpServletResponse) {
      //微信支付和支付宝支付的区别：
//        微信支付会保留在原页面,返回json格式;而支付宝支付会跳转页面,返回的是html格式;
        JsonData jsonData = productOrderService.confirm(confirmOrderRequest);
        if (jsonData.getCode() == 0) {
            //端类型  电脑端，手机端
            String clientType = confirmOrderRequest.getClientType();
            //微信或者支付宝
            String payType = confirmOrderRequest.getPayType();
            //如果是支付宝就会发生跳转,SDK除外;
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALI_PAY.name())) {
                if (clientType.equalsIgnoreCase(ClientTypeEnum.APP.name())) {
                    CommonUtil.sendJHtmlMessage(httpServletResponse, jsonData);
                } else if (clientType.equalsIgnoreCase(ClientTypeEnum.H5.name())) {

                } else if (clientType.equalsIgnoreCase(ClientTypeEnum.PC.name())) {

                }


            } else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT_APY.name())) {
                //微信支付;
                CommonUtil.sendJsonMessage(httpServletResponse, jsonData);
            } else {
                log.info("创建订单失败", jsonData);
                CommonUtil.sendJsonMessage(httpServletResponse, jsonData);
            }
        }
    }
}

