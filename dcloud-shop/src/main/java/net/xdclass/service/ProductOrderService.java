package net.xdclass.service;

import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.controller.request.ProductOrderPageRequest;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.model.EventMessage;
import net.xdclass.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.utils.JsonData;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MrWang
 * @since 2023-07-03
 */
public interface ProductOrderService {
   /**
    * @description TODO
    * 分页查询;
    * @return
    * @author
    * @date
    */
    Map<String, Object> page(ProductOrderPageRequest productOrderPageRequest);
    /**
     * @description TODO
     * 查询订单状态;
     * @return
     * @author
     * @date
     */
    String queryProductOrder(String outTradeNo);

    JsonData confirm(ConfirmOrderRequest confirmOrderRequest);
/**
 * @description TODO 
 * 关单服务;       
 * @return 
 * @author 
 * @date  
 */
    boolean closeProductOrder(EventMessage eventMessage);
   /**
    * @description TODO 
    * 处理微信回调通知;
    * @return 
    * @author 
    * @date  
    */
    JsonData processOrderCallBackMsg(ProductOrderPayTypeEnum wechatPay, Map<String, String> paramMap);

    /**
     * @description TODO
     * 处理队列里面订单相关消息;
     * @return
     * @author
     * @date
     */
    void handleProductMessage(EventMessage eventMessage);
}
