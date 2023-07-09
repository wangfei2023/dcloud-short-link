/**
 * @project dcloud-short-link
 * @description 对接支付宝
 * @author Administrator
 * @date 2023/7/9 0009 11:00:40
 * @version 1.0
 */

package net.xdclass.component;

import net.xdclass.vo.PayInfoVO;
import org.springframework.stereotype.Component;

@Component
public class AlipayStrategy implements PayStrategy{
    @Override
    public String unifiedOrder(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String refund(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String queryPayStatus(PayInfoVO payInfoVO) {
        return null;
    }

    @Override
    public String closeOrder(PayInfoVO payInfoVO) {
        return null;
    }
}


