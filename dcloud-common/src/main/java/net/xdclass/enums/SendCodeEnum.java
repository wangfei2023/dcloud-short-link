package net.xdclass.enums;

import lombok.Data;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/4 0004 21:09
 * @Version: 1.0
 * @Description:
 */

public enum SendCodeEnum {
      //用户注册;
      USER_REGISTER(1,"用户注册");
      private int code;
      private String info;

    SendCodeEnum(int code, String info) {
        this.code = code;
        this.info = info;
    }
}