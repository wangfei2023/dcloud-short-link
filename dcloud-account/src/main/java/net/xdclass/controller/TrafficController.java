/**
 * @project dcloud-short-link
 * @description 流量包管理
 * @author Administrator
 * @date 2023/7/11 0011 09:26:58
 * @version 1.0
 */

package net.xdclass.controller;

import net.xdclass.request.TrafficPageRequest;
import net.xdclass.request.UseTrafficRequest;
import net.xdclass.service.TrafficService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.TrafficVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic/v1")
public class TrafficController {
    @Autowired
    private TrafficService trafficService;
    /**
     * @description TODO 
     * 分页流量包查询列表，查看可用的;
     * @return 
     * @author 
     * @date  
     */
    @PostMapping("/page")
    public JsonData pageAvailable(@RequestBody TrafficPageRequest request){
       Map<String,Object> trafficMap= trafficService.page(request);
       return JsonData.buildSuccess(trafficMap);
    }

    /**
     * @description TODO
     * 查找某个流量包详情;
     * @return
     * @author
     * @date
     */
    @RequestMapping("/detail/{trafficId}")
    public JsonData detail(@PathVariable("trafficId") long trafficId){
       TrafficVo trafficVo = trafficService.detail(trafficId);
        return JsonData.buildSuccess(trafficVo);
    }


    /**
     * @description TODO
     * 使用流量包API
     * @return
     * @author
     * @date
     */
    @PostMapping("/reduce")
    public JsonData useTraffic(@RequestBody UseTrafficRequest useTrafficRequest, HttpServletRequest request){
       JsonData jsonData= trafficService.reduce(useTrafficRequest);
        return JsonData.buildSuccess(jsonData);
    }

}


