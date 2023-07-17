/**
 * @project dcloud-short-link
 * @description 流量包任务表
 * @author Administrator
 * @date 2023/7/13 0013 22:05:50
 * @version 1.0
 */

package net.xdclass.manager;

import net.xdclass.model.TrafficTaskDO;

public interface TrafficTaskManage {
     int add(TrafficTaskDO trafficTaskDO);

    TrafficTaskDO findByIdAndAccountNo(Long id,Long accountNo);

    int deleteByIdAndAccount(Long id,Long accountNo);
}


