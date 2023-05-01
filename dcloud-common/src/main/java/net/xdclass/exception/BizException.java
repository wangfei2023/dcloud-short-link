package net.xdclass.exception;

import lombok.Data;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/1 0001 18:50
 * @Version: 1.0
 * @Description:
 */
@Data
public class BizException extends RuntimeException {
   private String msg;
   private int code;
   public BizException (BizException bizException){
       //todo:调用父类的方法;
        super (bizException.getMessage());
       this.msg=bizException.getMessage();
       this.code=bizException.getCode();
   }
}