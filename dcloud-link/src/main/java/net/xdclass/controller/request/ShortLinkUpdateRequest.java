/**
 * @project dcloud-short-link
 * @description 分页查找某个分组下的短链数据
 * @author Administrator
 * @date 2023/6/13 0013 23:10:41
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class ShortLinkUpdateRequest {
    /*
 组
  */
    private int groupId;
    /*
  映射id
   */
    private int mappingId;
    /*
    短链码
     */
    private String code;

    /*
  标题
*/
    private String title;
    /*
  域名id
   */
    private Long domain;
    /*
   域名类型
     */
    private String domainType;
}


