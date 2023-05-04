package net.xdclass.service;

import net.xdclass.enums.SendCodeEnum;
import net.xdclass.utils.JsonData;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/3 0003 11:25
 * @Version: 1.0
 * @Description:
 */

public interface NotifyService {
    //todo:测试发送验证码;
    public void send ();

    JsonData sendCode(SendCodeEnum userRegister, String to);
}