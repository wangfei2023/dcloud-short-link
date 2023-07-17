package net.xdclass.feign;

import net.xdclass.utils.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="dclound-link-service")
public interface ShortLinkFeignService {
    /**
     * @description TODO 
     * 检查短链是否存在;
     * @return 
     * @author 
     * @date  
     */
    @GetMapping(value = "/api/link/v1/check",headers = "rpc-token=${rpc.token}")
    JsonData check(@RequestParam("shortLinkCode")  String shortLinkCode );
}
