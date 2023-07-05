package net.xdclass.service.impl;


import net.xdclass.config.InterceptorConfig;
import net.xdclass.controller.request.ConfirmOrderRequest;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.LoginUser;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.service.ProductOrderService;
import net.xdclass.utils.JsonData;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ProductOrderServiceImpl  implements ProductOrderService {
   @Autowired
   private ProductOrderManager productOrderManager;
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

    @Override
    public JsonData confirm(ConfirmOrderRequest confirmOrderRequest) {
        return null;
    }
}
