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
public class ShortLinkDelRequest {

    /**
     * 组
     */
    private Long groupId;

    /**
     * 映射id
     */
    private Long mappingId;


    /**
     * 短链码
     */
    private String code;
}


