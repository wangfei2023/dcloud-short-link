package net.xdclass.controller.request;

import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkAddRequest {
     /*
     组
      */
     private Long groupId;

      /*
      短链标题
      */
     private String title;

    /*
    原生的url
     */
    private String originalUrl;

      /*
   域名的id
    */
    private Long domainId;

    /*
   域名的类型
 */
    private String domainType;

    /*
    过期时间
   */
    private Date expired;
}
