package net.xdclass.request;

import lombok.Data;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/13 0013 23:30
 * @Version: 1.0
 * @Description:
 */
@Data
public class AccountLoginRequest {
    //登录手机号
  private String phone;
    //登录密码
    private String pwd;
}