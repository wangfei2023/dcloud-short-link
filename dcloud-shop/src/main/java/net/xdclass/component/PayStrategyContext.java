/**
 * @project dcloud-short-link
 * @description 使用策略者模式
 * @author Administrator
 * @date 2023/7/9 0009 11:04:33
 * @version 1.0
 */

package net.xdclass.component;

import net.xdclass.vo.PayInfoVO;

public class PayStrategyContext {
    private PayStrategy payStrategy;
    public PayStrategyContext(PayStrategy payStrategy){
        this.payStrategy=payStrategy;
    }

    /**
     * @description TODO
     * 根据策略对象,执行不同的下单接口;
     * @return
     * @author
     * @date
     */

    public String executeUnifiedOrder(PayInfoVO payInfoVO){
        return payStrategy.unifiedOrder(payInfoVO);
    }

    /**
     * @description TODO
     * 根据策略对象,执行不同的退款接口;
     * @return
     * @author
     * @date
     */

    public String executeRefund(PayInfoVO payInfoVO){
        return payStrategy.refund(payInfoVO);
    }

    /**
     * @description TODO
     * 根据策略对象,执行不同的关闭订单接口;
     * @return
     * @author
     * @date
     */

    public String executeCloseOrder(PayInfoVO payInfoVO){
        return payStrategy.closeOrder(payInfoVO);
    }

    /**
     * @description TODO
     * 根据策略对象,执行不同的查询支付状态接口;
     * @return
     * @author
     * @date
     */

    public String executeQueryPayStatus(PayInfoVO payInfoVO){
        return payStrategy.queryPayStatus(payInfoVO);
    }
}


