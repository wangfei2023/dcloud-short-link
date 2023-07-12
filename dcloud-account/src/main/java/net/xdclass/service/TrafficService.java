/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/10 0010 19:55:25
 * @version 1.0
 */

package net.xdclass.service;

import net.xdclass.model.EventMessage;
import net.xdclass.request.TrafficPageRequest;
import net.xdclass.request.UseTrafficRequest;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.TrafficVo;

import java.util.Map;

public interface TrafficService {
    void handlerTraffcMessage(EventMessage eventMessage);

    Map<String, Object> page(TrafficPageRequest request);

    TrafficVo detail(long trafficId);

    /**
     * @description TODO
     * 删除过期的流量包
     * @return
     * @author
     * @date
     */
    boolean deleteExpireTraffic();

    JsonData reduce(UseTrafficRequest useTrafficRequest);
}


