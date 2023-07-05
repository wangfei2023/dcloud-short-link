package net.xdclass.service;

import net.xdclass.controller.request.ConfirmOrderRequest;
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
    Map<String, Object> page(int page, int size, String status);
    /**
     * @description TODO
     * 查询订单状态;
     * @return
     * @author
     * @date
     */
    String queryProductOrder(String outTradeNo);

    JsonData confirm(ConfirmOrderRequest confirmOrderRequest);
}
