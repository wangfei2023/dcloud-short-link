/**
 * @project dcloud-short-link
 * @description 对接京东支付
 * @author Administrator
 * @date 2023/7/9 0009 11:01:53
 * @version 1.0
 */

package net.xdclass.component;

import net.xdclass.vo.PayInfoVO;

public class JdPayStrategy implements PayStrategy{
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


