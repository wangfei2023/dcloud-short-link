/**
 * @project dcloud-short-link
 * @description 简单工厂类
 * @author Administrator
 * @date 2023/7/9 0009 14:44:07
 * @version 1.0
 */

package net.xdclass.component;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.vo.PayInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayFactory {
    @Autowired
   private AlipayStrategy alipayStrategy;

    @Autowired
    private WechatPayStrategy wechatPayStrategy;

    public String pay(PayInfoVO payInfoVO){
        String payType = payInfoVO.getPayType();
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeUnifiedOrder(payInfoVO);
        }else if (ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
           return payStrategyContext.executeUnifiedOrder(payInfoVO);
        }
        return "";
    }

    public String closeOrder(PayInfoVO payInfoVO){
        String payType = payInfoVO.getPayType();
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return  payStrategyContext.executeCloseOrder(payInfoVO);
        }else if (ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeCloseOrder(payInfoVO);
        }
        return "";
    }



    public String queryPayStatus(PayInfoVO payInfoVO){
        String payType = payInfoVO.getPayType();
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeQueryPayStatus(payInfoVO);
        }else if (ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeQueryPayStatus(payInfoVO);
        }
        return "";
    }


    public String refund(PayInfoVO payInfoVO){
        String payType = payInfoVO.getPayType();
        if (ProductOrderPayTypeEnum.ALI_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(alipayStrategy);
            return payStrategyContext.executeRefund(payInfoVO);
        }else if (ProductOrderPayTypeEnum.WECHAT_PAY.name().equals(payType)){
            PayStrategyContext payStrategyContext = new PayStrategyContext(wechatPayStrategy);
            return payStrategyContext.executeRefund(payInfoVO);
        }
        return "";
    }

}


