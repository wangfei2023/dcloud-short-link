/**
 * @project dcloud-short-link
 * @description 时间范围内地区访问
 * @author Administrator
 * @date 2023/7/25 0025 22:50:02
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class RegionQueryRequest {
    private String code;

    private String startTime;

    private String endTime;
}


