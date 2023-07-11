/**
 * @project dcloud-short-link
 * @description 流量包分页查询
 * @author Administrator
 * @date 2023/7/11 0011 09:32:12
 * @version 1.0
 */

package net.xdclass.request;

import lombok.Data;

@Data
public class TrafficPageRequest {
    private int page;


    private int size;
}


