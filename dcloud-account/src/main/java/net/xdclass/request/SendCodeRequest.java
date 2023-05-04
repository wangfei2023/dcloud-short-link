package net.xdclass.request;

import lombok.Data;

/**
 * @Author: MrWang
 * @Contact: 1850195602@qq.com
 * @Date: 2023/5/4 0004 20:46
 * @Version: 1.0
 * @Description:发送请求类型;
 */
@Data
public class SendCodeRequest {
  //
  private String to;
  private String capcha;
}