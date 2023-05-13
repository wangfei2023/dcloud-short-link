package net.xdclass.service;

import net.xdclass.request.AccountRegisterRequest;
import net.xdclass.utils.JsonData;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/2 0002 0:09
 * @Version: 1.0
 * @Description:
 */

public interface AccountService {
    /**
     * 用户注册;
     *
     * 默认文件大小 1M,超过会报错
     *
     * @param accountRegisterRequest
     * @return
     */
    JsonData register(AccountRegisterRequest accountRegisterRequest);
}