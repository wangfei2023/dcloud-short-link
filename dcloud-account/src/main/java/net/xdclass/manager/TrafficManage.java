package net.xdclass.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xdclass.model.TrafficDO;

/**
 * @description TODO
 * 流量包管理开发模块
 * @return
 * @author
 * @date
 */
public interface TrafficManage {
    /**
     * @description TODO
     * 新增流量包
     * @return
     * @author
     * @date
     */
    int add(TrafficDO trafficDO);

    /**
     * @description TODO
     * 查询可用地流量包
     * @return
     * @author
     * @date
     */
    IPage<TrafficDO> pageAvaliable(int page,int size, long accountNo);


    /**
     * @description TODO
     * 查询详情
     * @return
     * @author
     * @date
     */

    TrafficDO findByIdAndAccountNo(long trafficId,long accountNo);


    /**
     * @description TODO
     * 增加某个流量包使用次数;
     * @return
     * @author
     * @date
     */
    int addDayUsedTimes(long currentTrafficId,long accountNo,int daysUsedTimes);
}
