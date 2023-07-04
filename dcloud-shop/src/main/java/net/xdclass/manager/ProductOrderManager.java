package net.xdclass.manager;

import net.xdclass.model.ProductOrderDO;

import java.util.Map;

public interface ProductOrderManager {
    /**
     * @description TODO 
     * 新增        
     * @return 
     * @author 
     * @date  
     */
    int add(ProductOrderDO productOrderDO);
    
    /**
     * @description TODO 
     * 通过订单号和账号查询;       
     * @return 
     * @author 
     * @date  
     */
    ProductOrderDO findOutTradeNoAndAccount(String outTradeNo,Long accountNo);
    /**
     * @description TODO 
     * 更新订单状态
     * @return 
     * @author 
     * @date  
     */
    int updateOrderPayState(String outTradeNo,Long accountNo,String newStatus,String oldStatus);

    /**
     * @description TODO
     * 根据订单状态查看;
     * @return
     * @author
     * @date
     */
    Map<String ,Object> selectPage(int page,int size,Long accountNo,String status);

    /**
     * @description TODO
     * 删除;
     * @return
     * @author
     * @date
     */
    public int del (long productOrderId,long accountNo );
}
