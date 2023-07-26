/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/25 0025 17:25:54
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;

@Data
public class VisitRecordPageRequest {
       private String code;
       private int page;
       private int size;
}


