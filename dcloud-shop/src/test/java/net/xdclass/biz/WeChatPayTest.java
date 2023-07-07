 /**
 * @project dcloud-short-link
 * @description流量包订单测试类;
 * @author Administrator
 * @date 2023/7/4 0004 00:00:04
 * @version 1.0
 */

package net.xdclass.biz;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.ShopApplication;
import net.xdclass.config.PayBeanConfig;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
@Slf4j
public class WeChatPayTest {
    @Autowired
    private PayBeanConfig payBeanConfig ;
    @Test
    public void testLoadPrivatKey() throws Exception{
        log.info(payBeanConfig.getPrivateKey().getAlgorithm());
    }

}


