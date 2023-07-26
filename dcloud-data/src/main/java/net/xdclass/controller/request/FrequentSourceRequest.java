/**
 * @project dcloud-short-link
 * @description 高频访问来源
 * @author Administrator
 * @date 2023/7/26 0026 15:43:28
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class FrequentSourceRequest {
    private String code;

    private String startTime;

    private String endTime;


}


