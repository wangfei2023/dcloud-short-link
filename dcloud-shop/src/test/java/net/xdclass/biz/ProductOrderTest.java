/**
 * @project dcloud-short-link
 * @description流量包订单测试类;
 * @author Administrator
 * @date 2023/7/4 0004 00:00:04
 * @version 1.0
 */

package net.xdclass.biz;

import net.xdclass.ShopApplication;
import net.xdclass.manager.ProductOrderManager;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.model.ProductOrderDO;
import net.xdclass.utils.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShopApplication.class)
public class ProductOrderTest {
    @Autowired
    private ProductOrderManager productOrderManager;
    @Test
    public void testAdd(){
        for (int i = 0; i < 5; i++) {
            ProductOrderDO productOrderDO = ProductOrderDO.builder()
                    .outTradeNo(CommonUtil.generateUUID())
                    .payAmount(new BigDecimal(11))
                    .state("OLD")
                    .nickname("我是最弱的")
                    .accountNo(10L)
                    .del(0)
                    .productId(2l).build();
            productOrderManager.add(productOrderDO);
        }
    }
    @Test
    public void testSelectPage(){
        Map<String, Object> map = productOrderManager.selectPage(1, 1, (long) 10, "NEW");
        System.out.println(map);
    }
}


