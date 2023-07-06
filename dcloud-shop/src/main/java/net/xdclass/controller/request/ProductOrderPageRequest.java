/**
 * @project dcloud-short-link
 * @description 分页查找某个分组下的短链数据
 * @author Administrator
 * @date 2023/6/13 0013 23:10:41
 * @version 1.0
 */

package net.xdclass.controller.request;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class ProductOrderPageRequest {
    private int page;

    private int size;

    private String status;
}


