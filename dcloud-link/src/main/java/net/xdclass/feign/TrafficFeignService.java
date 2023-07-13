package net.xdclass.feign;

import net.xdclass.controller.request.UseTrafficRequest;
import net.xdclass.utils.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "dclound-account-service")
public interface TrafficFeignService {
    @PostMapping(value = "/api/traffic/v1/reduce",headers = {"rpc-token=${rpc.token}"})
    public JsonData useTraffic(@RequestBody UseTrafficRequest useTrafficRequest);
}
