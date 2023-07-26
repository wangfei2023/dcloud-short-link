package net.xdclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xdclass.model.ShortLinkVisitStatsDO;
import net.xdclass.vo.ShortLinkVisitStatsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VisitStatsMapper extends BaseMapper<ShortLinkVisitStatsDO> {
//      <!--统计总条数-->
    int countTotal(@Param("code") String code, @Param("accountNo")Long accountNo);
//    <!--分页查找-->
    List<ShortLinkVisitStatsDO> pageVisitRecord(@Param("code") String code,  @Param("accountNo") Long accountNo,  @Param("from") int from, @Param("size") int size);

    List<ShortLinkVisitStatsDO> queryRegionVisitStatsWithDay(@Param("code")  String code,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("accountNo") Long accountNo);
    /**
     * @description TODO
     * 查询时间范围内访问趋势图;多天级别
     * @return
     * @author
     * @date
     */
    List<ShortLinkVisitStatsDO> queryVisitTrendWithMultiDay(@Param("code") String code,@Param("type") String type, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("accountNo") Long accountNo);
    /**
     * @description TODO
     * 查询时间范围内访问趋势图;分钟级别
     * @return
     * @author
     * @date
     */
    List<ShortLinkVisitStatsDO> queryVisitTrendWithMinute(@Param("code") String code,@Param("type") String type, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("accountNo") Long accountNo);
/**
 * @description TODO 
 * 查询时间范围内访问趋势图;小时级别
 * @return 
 * @author 
 * @date  
 */
    List<ShortLinkVisitStatsDO> queryVisitTrendWithHour(@Param("code") String code,@Param("type") String type, @Param("startTime") String startTime, @Param("accountNo") Long accountNo);
}
