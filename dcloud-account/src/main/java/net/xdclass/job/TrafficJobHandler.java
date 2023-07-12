/**
 * @project dcloud-short-link
 * @description XXL-Job分布式调度任务测试
 * @author Administrator
 * @date 2023/7/12 0012 08:49:09
 * @version 1.0
 */

package net.xdclass.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrafficJobHandler {
    @Autowired
    private TrafficService trafficService;
    @XxlJob(value = "TrafficExpireJobHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){
         log.info("execute触发成功");
        trafficService.deleteExpireTraffic();
         return ReturnT.SUCCESS;
    }
    public void init(){
        log.info(" MyJobHandler init >>>>>");
    }
    private void destroy(){
        log.info(" MyJobHandler destroy >>>>>");
    }
}


