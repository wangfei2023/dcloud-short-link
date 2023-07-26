/**
 * @project dcloud-short-link
 * @description -天维度访问曲线图
 * @author Administrator
 * @date 2023/7/26 0026 09:47:29
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class VisitTrendQueryRequest {
    private String code;
    /**
     * 跨天、当天24小时、分钟级别
     */
    private String type;

    private String startTime;

    private String endTime;

}


