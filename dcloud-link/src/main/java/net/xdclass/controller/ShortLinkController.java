package net.xdclass.controller;


import net.xdclass.controller.request.ShortLinkAddRequest;
import net.xdclass.controller.request.ShortLinkDelRequest;
import net.xdclass.controller.request.ShortLinkPageRequest;
import net.xdclass.controller.request.ShortLinkUpdateRequest;
import net.xdclass.service.ShortLinkService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.ShortLinkVo;
import org.bouncycastle.cms.PasswordRecipientId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author MrWang
 * @since 2023-05-23
 */
//b端开发;
@RestController
@RequestMapping("/api/link/v1")
public class ShortLinkController {
    @Autowired
    private ShortLinkService shortLinkService;

    @Value("${rpc.token}")
    private String rpcToken;

    @PostMapping("/add")
     public JsonData createShortLink(@RequestBody  ShortLinkAddRequest request){
        JsonData jsonData= shortLinkService.createShortLink(request);
        return jsonData;
     }
    /**
     * @description TODO
     * 分页查找短链
     * @return
     * @author
     * @date
     *
     */
    @PostMapping("/page")
    public JsonData  pageShortLinkByGroupId(@RequestBody ShortLinkPageRequest request){
       Map<String,Object> pageResult=shortLinkService.pageShortLinkByGroupId(request);
       return JsonData.buildSuccess(pageResult);
    }
    /**
     * @description TODO
     *删除短链
     * @return
     * @author
     * @date
     */
    @PostMapping("/del")
    public JsonData  del(@RequestBody ShortLinkDelRequest request){
        JsonData jsonData=shortLinkService.del(request);
        return JsonData.buildSuccess(jsonData);
    }

    /**
     * @description TODO
     *更新短链消费者
     * @return
     * @author
     * @date
     */
    @PostMapping("/update")
    public JsonData  update (@RequestBody ShortLinkUpdateRequest request){
        JsonData jsonData=shortLinkService.update(request);
        return JsonData.buildSuccess(jsonData);
    }

    /**
     * @description TODO
     * 检查短链是否存在;
     * @return
     * @author
     * @date
     */
    @GetMapping( "/check")
    JsonData check(@RequestParam("shortLinkCode")  String shortLinkCode, HttpServletRequest request){
        String token = request.getHeader("rpc-token");
        if (rpcToken.equals(token)){
            ShortLinkVo shortLinkVo = shortLinkService.parseLinkCode(shortLinkCode);
           return shortLinkVo==null?JsonData.buildError("短链不存在"):JsonData.buildSuccess();
        }else {
           return JsonData.buildError("非法访问");
        }
    }
}

