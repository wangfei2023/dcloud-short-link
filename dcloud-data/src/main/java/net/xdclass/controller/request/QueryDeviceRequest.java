/**
 * @project dcloud-short-link
 * @description查询设备
 * @author Administrator
 * @date 2023/7/26 0026 16:21:39
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class QueryDeviceRequest {
    private String code;

    private String startTime;

    private String endTime;
}


