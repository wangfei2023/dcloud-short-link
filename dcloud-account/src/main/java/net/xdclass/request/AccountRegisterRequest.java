package net.xdclass.request;

import lombok.Data;

import java.util.Date;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/13 0013 18:17
 * @Version: 1.0
 * @Description:用户注册功能;
 */
@Data
public class AccountRegisterRequest {


    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String pwd;



    /**
     * 邮箱
     */
    private String mail;

    /**
     * 用户名
     */
    private String username;
    /**
     * 验证码
     *
     */
    private String code;
}