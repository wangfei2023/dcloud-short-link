/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/25 0025 17:13:12
 * @version 1.0
 */

package net.xdclass.controller;

import net.xdclass.controller.request.RegionQueryRequest;
import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.controller.request.VisitTrendQueryRequest;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.VisitStatsService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.ShortLinkVisitStatsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/visit_state/v1")
public class VisitStatsController {
    @Autowired
    private VisitStatsService visitStatsService;
   @PostMapping("page_record")
    public JsonData pageVisitRecord(@RequestBody VisitRecordPageRequest request){
        //条数限制
        int total = request.getSize() * request.getPage();
        if (total>1000){
            return JsonData.buildResult(BizCodeEnum.DATA_OUT_OF_SIZE);
        }
        Map<String,Object>pageResult=visitStatsService.pageVistRecord(request);
        return JsonData.buildSuccess(pageResult);
    }
    /**
     * @description TODO
     * 查询时间范围内地区访问分布次数
     * @return
     * @author
     * @date
     */
    @RequestMapping("region_day")
    public JsonData queryRegionWithDay(@RequestBody RegionQueryRequest request){
        List<ShortLinkVisitStatsVo> list = visitStatsService.queryRegionWithDay(request);
        return JsonData.buildSuccess(list);
    }

    /**
     * @description TODO
     * 访问趋势图
     * @return
     * @author
     * @date
     */
    @RequestMapping("trend")
    public JsonData queryVisitTrend(@RequestBody VisitTrendQueryRequest request ){
        List<ShortLinkVisitStatsVo>list = visitStatsService.queryVisitTrend(request);
        return JsonData.buildSuccess(list);
    }
}


