package net.xdclass.service.impl;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.config.InterceptorConfig;
import net.xdclass.constant.TimeConstant;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.enums.BillTypeEnum;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.enums.ProductOrderStateEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.ProductManager;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.LoginUser;
import net.xdclass.model.ProductDO;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.service.ProductOrderService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import net.xdclass.vo.PayInfoVO;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.smartcardio.CommandAPDU;
import java.math.BigDecimal;
import java.util.Date;
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
    @Override
    public Map<String, Object> page(int page, int size, String status) {
        long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        Map<String, Object> selectPage = productOrderManager.selectPage(page, size, accountNo, status);
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

        //调用支付信息;TODO:
        return null;
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
