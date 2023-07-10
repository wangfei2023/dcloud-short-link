/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:56:01
 * @version 1.0
 */

package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.EventMessageType;
import net.xdclass.manager.TrafficManage;
import net.xdclass.model.EventMessage;
import net.xdclass.model.TrafficDO;
import net.xdclass.service.TrafficService;
import net.xdclass.utils.JsonUtil;
import net.xdclass.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * @description TODO
 *   Map<String, Object> content = new HashMap<>(4);
 *         content.put("outTradeNo", outTradeNo);
 *         content.put("buyNum", productOrderDO.getBuyNum());
 *         content.put("accountNo", productOrderDO.getAccountNo());
 *         content.put("product", JsonUtil.obj2Json(productOrderDO.getProductSnapshot()));
 * @return
 * @author
 * @date
 */
@Service
@Slf4j
public class TrafficServiceImpl implements TrafficService {
    @Autowired
    private TrafficManage trafficManage;
    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void handlerTraffcMessage(EventMessage eventMessage) {
        String eventMessageType = eventMessage.getEventMessageType();
        if (EventMessageType.PRODUCT_ORDER_PAY.name().equalsIgnoreCase(eventMessageType)){
            String content = eventMessage.getContent();
            Map orderInfoMap = JsonUtil.json2Obj(content, Map.class);
            //空间换时间;将订单信息存储在mq里面;
            long accountNo =(Long) orderInfoMap.get("accountNo");
            String outTradeNo =(String) orderInfoMap.get("outTradeNo");
            Integer buyNum =(Integer) orderInfoMap.get("buyNum");
            String productStr =(String) orderInfoMap.get("product");
            ProductVo productVo = JsonUtil.json2Obj(productStr, ProductVo.class);
            log.info("商品信息",productVo);
           //流量包有效期;
            LocalDateTime expireDateTime = LocalDateTime.now().plusDays(productVo.getValidDay());
            java.util.Date date = Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant());
            TrafficDO trafficDO = TrafficDO.builder()
                    .accountNo(accountNo)
                    .dayLimit(productVo.getDayTimes() * buyNum)
                    .totalLimit(productVo.getTotalTimes())
                    .pluginType(productVo.getPluginType())
                    .level(productVo.getLevel())
                    .productId(productVo.getId())
                    .outTradeNo(outTradeNo)
                    .expiredDate(date)
                    .build();
            int rows = trafficManage.add(trafficDO);
            log.info("消费消息新增流量包:rows={},trafficDO={}",rows,trafficDO);
        }
    }
}


