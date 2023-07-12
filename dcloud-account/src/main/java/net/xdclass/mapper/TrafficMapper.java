package net.xdclass.mapper;


import net.xdclass.model.TrafficDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MrWang
 * @since 2023-05-01
 */
public interface TrafficMapper extends BaseMapper<TrafficDO> {

    int addDayUsedTimes(@Param("accountNo") Long accountNo,
                        @Param("trafficId") Long trafficId,
                        @Param("usedTimes") Integer usedTimes);
//usedTimes已经用的流量包的次数;
    int releaseUsedTimes(@Param("accountNo") Long accountNo,
                         @Param("trafficId")  Long trafficId,
                         @Param("useTimes") Integer usedTimes);
}
