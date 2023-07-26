package net.xdclass.service;

import net.xdclass.controller.request.*;
import net.xdclass.model.ShortLinkVisitStatsDO;
import net.xdclass.vo.ShortLinkVisitStatsVo;

import java.util.List;
import java.util.Map;

public interface VisitStatsService {
    /**
     * @description TODO 
     *    分页实时查看访问记录
     * @return 
     * @author 
     * @date  
     */
    Map<String, Object> pageVistRecord(VisitRecordPageRequest request);

    List<ShortLinkVisitStatsVo> queryRegionWithDay(RegionQueryRequest request);

    List<ShortLinkVisitStatsVo> queryVisitTrend(VisitTrendQueryRequest request);

    List<ShortLinkVisitStatsVo> queryFrequentSource(FrequentSourceRequest request);

    Map<String,List<ShortLinkVisitStatsVo>>  queryDeviceInfo(QueryDeviceRequest request);
}
