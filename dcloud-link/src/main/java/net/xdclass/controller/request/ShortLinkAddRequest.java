package net.xdclass.controller.request;

import lombok.Data;

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
    private String domainId;

    /*
   域名的类型
 */
    private String domainType;

    /*
    过期时间
   */
    private String expired;
}
