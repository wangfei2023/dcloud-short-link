package net.xdclass.controller;

import com.google.code.kaptcha.Producer;
import net.xdclass.config.CaptchaConfig;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.request.SendCodeRequest;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private Producer producer;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final long EXPER_TIME=1000*60*10;
   //用于测试
    @RequestMapping("send")
    public JsonData sendCode(){
       notifyService.send();
       return JsonData.buildSuccess();
   }
    //todo:做缓存验证码,缓存到redis里面;
   @GetMapping("/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
       String text = producer.createText();
       stringRedisTemplate.opsForValue().set(getCaptchaKey(request),text,EXPER_TIME, TimeUnit.MILLISECONDS);
       BufferedImage image = producer.createImage(text);
       try {
           ServletOutputStream stream = response.getOutputStream();
           ImageIO.write(image,"jpg",stream);
           stream.flush();
           stream.close();
       } catch (IOException e) {
           e.printStackTrace();
       }


   }

   //todo:发送短信验证码;
    @PostMapping("send_code")
    public JsonData sendCode(@RequestBody SendCodeRequest sendCodeRequest,HttpServletRequest request ){
        String captchaKey = getCaptchaKey(request);
        //todo:判断redis缓存是否有验证码;
        String catchcaptchaKey = stringRedisTemplate.opsForValue().get(captchaKey);

        //todo：用户请求获取验证码;
        String capcha = sendCodeRequest.getCapcha();
        if (!ObjectUtils.isEmpty(catchcaptchaKey)&&!ObjectUtils.isEmpty(capcha)&&catchcaptchaKey.equalsIgnoreCase(capcha)){
            stringRedisTemplate.delete(catchcaptchaKey);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER,sendCodeRequest.getTo());
            return jsonData;
        }else{
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }
      /*
   公共方法;获取请求IP地址;
      */
    public String getCaptchaKey(HttpServletRequest request){
        String ipAddr = CommonUtil.getIpAddr(request);
        String header = request.getHeader("User-Agent");
        String key="account-service"+CommonUtil.MD5(ipAddr+header);
        return key;
    }


}