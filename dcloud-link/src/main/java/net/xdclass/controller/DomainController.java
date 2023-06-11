package net.xdclass.controller;

import net.xdclass.service.DomainService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.DomainVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author : [Administrator]
 * @version : [v1.0]
 * @description : [一句话描述该类的功能]
 * @createTime : [2023/5/29 0029 23:02]
 * @updateUser : [Administrator]
 * @updateTime : [2023/5/29 0029 23:02]
 * @updateRemark : [说明本次修改内容]
 */
@RestController
@RequestMapping("/api/domain/v1")
public class DomainController {
    @Autowired
    private DomainService domainService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @GetMapping("list")
    public JsonData list(){
        List<DomainVo>list= domainService.listAll();
        return JsonData.buildSuccess();
    }
    /**
     * @description Lua脚本分布式重入锁+redis原生代码编写
     *
     * @return
     * @author
     * @date
     */
    @GetMapping("test")
    public JsonData test(@RequestParam(name = "code") String code,
                         @RequestParam(name = "accountNo") String accountNo

    ){
        //key1是短链码，ARGV[1]是accountNo,ARGV[2]是过期时间
        String script = "if redis.call('EXISTS',KEYS[1])==0 then redis.call('set',KEYS[1],ARGV[1]); redis.call('expire',KEYS[1],ARGV[2]); return 1;" +
                " elseif redis.call('get',KEYS[1]) == ARGV[1] then return 2;" +
                " else return 0; end;";

        Long result = redisTemplate.execute(new
                DefaultRedisScript<>(script, Long.class), Arrays.asList(code), accountNo,100);
        return JsonData.buildSuccess(result);
    }

}
