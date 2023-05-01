package net.xdclass.exception;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/1 0001 19:02
 * @Version: 1.0
 * @Description:
 */
//RestControllerAdvice不需要加 @RequestBody注解;
@RestControllerAdvice
@Slf4j
public class CusTomExceptionHandle {
   @ExceptionHandler(value = Exception.class)
    public JsonData handler(Exception e){
       if (e instanceof BizException){
           BizException bizException=(BizException)e;
           log.info("业务异常",e);
           return JsonData.buildCodeAndMsg(bizException.getCode(),bizException.getMsg());
       }else {
           log.info("系统异常",e);
           return JsonData.buildError("系统异常");
       }
   }

}