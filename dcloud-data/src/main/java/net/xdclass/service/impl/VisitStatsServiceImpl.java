/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/25 0025 17:14:43
 * @version 1.0
 */

package net.xdclass.service.impl;

import net.xdclass.controller.request.RegionQueryRequest;
import net.xdclass.controller.request.VisitRecordPageRequest;
import net.xdclass.controller.request.VisitTrendQueryRequest;
import net.xdclass.enums.DateTimeFieldEnum;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.VisitStatsMapper;
import net.xdclass.model.ShortLinkVisitStatsDO;
import net.xdclass.service.VisitStatsService;
import net.xdclass.vo.ShortLinkVisitStatsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class VisitStatsServiceImpl implements VisitStatsService {
    @Autowired
    private VisitStatsMapper visitStatsMapper;
    @Override
    public Map<String, Object> pageVistRecord(VisitRecordPageRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        Map<String, Object> data = new HashMap<>(16);

        String code = request.getCode();
        int page = request.getPage();
        int size = request.getSize();

        int count = visitStatsMapper.countTotal(code, accountNo);
        int from = (page - 1) * size;

        List<ShortLinkVisitStatsDO> list = visitStatsMapper.pageVisitRecord(code, accountNo, from, size);
        List<ShortLinkVisitStatsVo> visitStatsVoList = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        data.put("total", count);
        data.put("current_page", page);

        /**计算总页数*/
        int totalPage = 0;
        if (count % size == 0) {
            totalPage = count / size;
        } else {
            totalPage = count / size + 1;
        }
        data.put("total_page", totalPage);

        data.put("data", visitStatsVoList);
        return data;
    }
    //时间先不做限制，正常需要限制
    @Override
    public List<ShortLinkVisitStatsVo> queryRegionWithDay(RegionQueryRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        List<ShortLinkVisitStatsDO> list = visitStatsMapper.queryRegionVisitStatsWithDay(request.getCode(), request.getStartTime(), request.getEndTime(), accountNo);
        List<ShortLinkVisitStatsVo> linkVisitStatsVos = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return linkVisitStatsVos;
    }

    @Override
    public List<ShortLinkVisitStatsVo> queryVisitTrend(VisitTrendQueryRequest request) {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();
        String code = request.getCode();
        String type = request.getType();
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        List<ShortLinkVisitStatsDO> list=null;
        if (DateTimeFieldEnum.DAY.name().equalsIgnoreCase(type)){
         list =  visitStatsMapper.queryVisitTrendWithMultiDay(code,type,startTime,endTime,accountNo);
        }else if(DateTimeFieldEnum.HOUR.name().equalsIgnoreCase(type)){
         list= visitStatsMapper.queryVisitTrendWithHour(code,type,startTime,accountNo);
        }else if (DateTimeFieldEnum.MINUTE.name().equalsIgnoreCase(type)){
         list= visitStatsMapper.queryVisitTrendWithMinute(code,type,startTime,endTime,accountNo);
        }
        List<ShortLinkVisitStatsVo> visitStatsVos = list.stream().map(obj -> beanProcess(obj)).collect(Collectors.toList());
        return visitStatsVos;
    }

    public ShortLinkVisitStatsVo beanProcess(ShortLinkVisitStatsDO shortLinkVisitStatsDO ){
        ShortLinkVisitStatsVo shortLinkVisitStatsVo = new ShortLinkVisitStatsVo();
        BeanUtils.copyProperties(shortLinkVisitStatsDO,shortLinkVisitStatsVo);
        return shortLinkVisitStatsVo;
    }
}


