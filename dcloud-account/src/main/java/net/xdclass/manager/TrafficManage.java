package net.xdclass.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.xdclass.model.TrafficDO;

import java.util.List;

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
     * 删除过期的流量包
     * @return
     * @author
     * @date
     */
    boolean deleteExpireTraffic();

    /**
     * 查找可用的短链流量包(未过期),包括免费流量包
     * @param accountNo
     * @return
     */
    List<TrafficDO> selectAvailableTraffics( Long accountNo);

    /**
     * 给某个流量包增加使用次数
     *
     * @param currentTrafficId
     * @param accountNo
     * @param usedTimes
     * @return
     */
    int addDayUsedTimes(Long accountNo, Long trafficId, Integer usedTimes) ;

    /**
     * 恢复流量包使用当天次数
     * @param accountNo
     * @param trafficId
     * @param usedTimes
     */
    int releaseUsedTimes(Long accountNo, Long trafficId, Integer usedTimes);


    /**
     * 批量更新流量包使用次数为0(计算日用量)
     * @param accountNo
     * @param unUpdatedTrafficIds
     */
    int batchUpdateUsedTimes(Long accountNo, List<Long> unUpdatedTrafficIds);
}
