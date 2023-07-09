/**
 * @project dcloud-short-link
 * @description 对接微信
 * @author Administrator
 * @date 2023/7/9 0009 10:57:15
 * @version 1.0
 */

package net.xdclass.component;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.vo.PayInfoVO;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WechatPayStrategy  implements PayStrategy{
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


