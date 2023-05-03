package net.xdclass.controller;

import net.xdclass.service.NotifyService;
import net.xdclass.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/3 0003 11:18
 * @Version: 1.0
 * @Description:
 */
@RestController
@RequestMapping("/api/v1/Notify")
public class NotifyController {
    @Autowired
    private NotifyService notifyService;
   @RequestMapping("send_code")
    public JsonData sendCode(){
       JsonData send = notifyService.send();
       return JsonData.buildSuccess(send);
   }
}