package net.xdclass.service.impl;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.component.PayFactory;
import net.xdclass.config.InterceptorConfig;
import net.xdclass.config.RabbitMQConfig;
import net.xdclass.constant.TimeConstant;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.controller.request.ProductOrderPageRequest;
import net.xdclass.enums.*;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.ProductManager;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.EventMessage;
import net.xdclass.model.LoginUser;
import net.xdclass.model.ProductDO;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.service.ProductOrderService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import net.xdclass.vo.PayInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.CommandAPDU;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author MrWang
 * @since 2023-07-03
 */
@Service
@Slf4j
public class ProductOrderServiceImpl  implements ProductOrderService {
   @Autowired
   private ProductOrderManager productOrderManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    private PayFactory payFactory;
    @Override
    public Map<String, Object> page(ProductOrderPageRequest request) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> selectPage = productOrderManager.selectPage(request.getPage(), request.getSize(), accountNo, request.getStatus());
        return selectPage;
    }

    @Override
    public String queryProductOrder(String outTradeNo) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        ProductOrderDO productOrderDO = productOrderManager.findOutTradeNoAndAccount(outTradeNo, accountNo);
//        if (productOrderDO==null){
//            return "";
//        }else{
//            return productOrderDO.getState();
//        }
        return productOrderDO==null?"":productOrderDO.getState();
    }


/**
 * @description TODO
 *      /
 * *  重防􏰀提交（TODO）
 *             *  获取最新的流量包价格
 * *  订单验价
 *    *  如果有优惠券或者其他抵扣
 *    *  验证前端显示和后台计算价格
 * *  创建订单对象保存数据库
 * *  发送延迟消息-用于自动关单（TODO）
 * *  创建支付信息-对接三方支付（TODO）
 *  *  回调更新订单状态（TODO）
 *  *  支付成功创建流量包（TODO）
 * @return
 * @author
 * @date
 */
    @Override
    @Transactional
    public JsonData confirm(ConfirmOrderRequest confirmOrderRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //订单号随机生成;
        String outTradeOrderNo = CommonUtil.getStringNumRandom(32);
        ProductDO productDO = productManager.findDetailById(confirmOrderRequest.getProductId());
        //校验前后端传入的价格;
        this.check(productDO,confirmOrderRequest);


        //创建订单;
        ProductOrderDO productOrderDO= this.saveProductOrder(confirmOrderRequest,loginUser,outTradeOrderNo,productDO);

        //创建支付信息;
        PayInfoVO payInfoVO = PayInfoVO.builder()
                .outTradeNo(outTradeOrderNo)
                .payFee(confirmOrderRequest.getPayAmount())
                .payType(confirmOrderRequest.getPayType())
                .clientType(confirmOrderRequest.getClientType())
                .title(productDO.getTitle())
                .description(productDO.getDetail())
                .orderPayTimeoutMills(TimeConstant.ORDER_PAY_TIMEOUT_MILLS)
                .accountNo(loginUser.getAccountNo())
                .build();
        //调用延迟消息;TODO:
        EventMessage eventMessage = EventMessage.builder()
                .eventMessageType(EventMessageType.PRODUCT_ORDER_NEW.name())
                .accountNo(loginUser.getAccountNo())
                .bizId(outTradeOrderNo)
                .build();

        rabbitTemplate.convertAndSend(rabbitMQConfig.getOrderEventExchange(),rabbitMQConfig.getOrderCloseDelayRoutingKey(),eventMessage);
        //调用支付信息;TODO:
        String orderUrl = payFactory.pay(payInfoVO);
        if (StringUtils.isNotBlank(orderUrl)){
            HashMap<Object, Object> hashMap = new HashMap<>(2);
            hashMap.put("code_url",orderUrl);
            hashMap.put("out_trade_no",payInfoVO.getOutTradeNo());
            return JsonData.buildSuccess(hashMap);
        }
        return JsonData.buildResult(BizCodeEnum.PAY_ORDER_FAIL);
    }
    /**
     * //延迟消息的时间 需要比订单过期 时间长一点，这样就不存在查询的时候，用户还能支付成功
     *
     * //查询订单是否存在，如果已经支付则正常结束
     * //如果订单未支付，主动调用第三方支付平台查询订单状态
     *     //确认未支付，本地取消订单
     *     //如果第三方平台已经支付，主动的把订单状态改成已支付，造成该原因的情况可能是支付通道回调有问题，然后触发支付后的动作，如何触发？RPC还是？
     * @param eventMessage
     */

    @Override
    public boolean closeProductOrder(EventMessage eventMessage) {
        Long accountNo = eventMessage.getAccountNo();
        String outTradeNo = eventMessage.getBizId();
        ProductOrderDO productOrderDO = productOrderManager.findOutTradeNoAndAccount(outTradeNo, accountNo);
        if (productOrderDO==null){
            log.info("订单不存在");
            return true;
        }
        if (productOrderDO.getState().equalsIgnoreCase(ProductOrderStateEnum.PAY.name())){
            log.info("直接确认消息,订单已经支付{}",eventMessage);
               return true;
        }
        if (productOrderDO.getState().equalsIgnoreCase(ProductOrderStateEnum.NEW.name())){
            //向第三方查询状态;
            PayInfoVO payInfoVO = new PayInfoVO();
            payInfoVO.setAccountNo(accountNo);
            payInfoVO.setPayType(productOrderDO.getPayType());
            payInfoVO.setOutTradeNo(outTradeNo);

            //todo:需要向第三方支付平台查询状态;
            String payResult="";
            if (StringUtils.isEmpty(payResult)){
                //如果为空,则未支付成功,本地取消订单;
                productOrderManager.updateOrderPayState(outTradeNo,accountNo,ProductOrderStateEnum.CANCEL.name(),ProductOrderStateEnum.NEW.name());
                log.info("未支付订单,本地取消订单：{}",eventMessage);
            }else {
                log.warn("支付成功,但是微信回调通知失败,需排查问题：{}",eventMessage);
                productOrderManager.updateOrderPayState(outTradeNo,accountNo,ProductOrderStateEnum.PAY.name(),ProductOrderStateEnum.NEW.name());
                //触发支付成功后的逻辑;//TODO:


            }
        }
        return true;
    }


    public void check( ProductDO productDO,ConfirmOrderRequest confirmOrderRequest) {
       //计算后端的价格;
        BigDecimal totleMoney= BigDecimal.valueOf(confirmOrderRequest.getBuyNum()).multiply(productDO.getAmount());
        //验证前后端价格是否一值，如果有优惠券也在这里计算
        if (totleMoney.compareTo(confirmOrderRequest.getPayAmount())!=0){
             log.info("验价失败{}",confirmOrderRequest);
             throw new BizException(BizCodeEnum.ORDER_CONFIRM_PRICE_FAIL);
        }
    }

    private ProductOrderDO saveProductOrder(ConfirmOrderRequest confirmOrderRequest, LoginUser loginUser, String outTradeOrderNo, ProductDO productDO) {
        ProductOrderDO productOrderDO= ProductOrderDO.builder().
                //设置用户信息;
               accountNo(loginUser.getAccountNo())
              .nickname(loginUser.getUsername())
              //设置商品信息;
             .productId(productDO.getId())
             .productTitle(productDO.getTitle())
             .productSnapshot(JsonUtil.obj2Json(productDO))
             .productAmount(productDO.getAmount())
             //设置订单信息;
             .buyNum(confirmOrderRequest.getBuyNum())
             .outTradeNo(outTradeOrderNo)
             .createTime(new Date())
             .del(0)

            //发票信息：
             .billType(BillTypeEnum.valueOf(confirmOrderRequest.getBillType()).name())
             .billHeader(confirmOrderRequest.getBillHeader())
             .billReceiverPhone(confirmOrderRequest.getBillReceiverPhone())
             .billReceiverEmail(confirmOrderRequest.getBillReceiverEmail())
             .billContent(confirmOrderRequest.getBillContent())
                //实际支付总价
                .payAmount(confirmOrderRequest.getPayAmount())
             .totalAmount(confirmOrderRequest.getTotalAmount())
             .state(ProductOrderStateEnum.NEW.name())
                //支付类型;
             .payType(ProductOrderPayTypeEnum.valueOf(confirmOrderRequest.getPayType()).name())
             .build();
            productOrderManager.add(productOrderDO);
            return productOrderDO;
    }


}
