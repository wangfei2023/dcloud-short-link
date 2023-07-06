package net.xdclass.constant;

import lombok.Data;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/4 0004 22:08
 * @Version: 1.0
 * @Description:
 */
public class RedisKey {
    //todo:第一个%s表示类型,用户注册或用户发送,第二个%s表示用手机号或是邮箱;
   public static final String CHECK_CODE_KEY="code:%s:%s";

    public static final String SUBMIT_ORDER_TOKEN_KEY="order:submit:%s:%s";
}