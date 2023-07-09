package net.xdclass.component;

import net.xdclass.vo.PayInfoVO;

/**
 * @description TODO 
 * 多渠道支付对接-策略模式
 * @return 
 * @author 
 * @date  
 */
public interface PayStrategy {
      /**
       * @description TODO
       * 统一下单接口
       * @return
       * @author
       * @date
       */
      String  unifiedOrder(PayInfoVO payInfoVO);

    /**
     * @description TODO
     * 退款接口
     * @return
     * @author
     * @date
     */

    default  String refund(PayInfoVO payInfoVO){
        return "";
    }

    /**
     * @description TODO
     * 查询支付状态
     * @return
     * @author
     * @date
     */
    default  String queryPayStatus(PayInfoVO payInfoVO){
        return "";
    }


    /**
     * @description TODO
     * 关闭订单
     * @return
     * @author
     * @date
     */
    default  String closeOrder(PayInfoVO payInfoVO){
        return "";
    }
}
