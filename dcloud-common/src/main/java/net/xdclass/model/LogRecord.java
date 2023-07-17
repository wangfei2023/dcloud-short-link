/**
 * @project dcloud-short-link
 * @description 日志记录
 * @author Administrator
 * @date 2023/7/17 0017 20:10:26
 * @version 1.0
 */

package net.xdclass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogRecord {
    /**
     * @description TODO
     * 客户端ip
     * @return
     * @author
     * @date
     */
    private String ip;
    /**
     * @description TODO
     * 产生的时间戳
     * @return
     * @author
     * @date
     */
    private Long timeStamp;

    /**
     * @description TODO
     * 日志时间类型
     * @return
     * @author
     * @date
     */
    private String event;

    /**
     * @description TODO
     * udid,是设备的唯一标识,全称 uniqueDeviceIdentifier
     * @return
     * @author
     * @date
     */

    private String udid;

    /**
     * @description TODO
     * 业务id
     * @return
     * @author
     * @date
     */
    private String bizid;

    /**
     * @description TODO
     * 日志内容
     * @return
     * @author
     * @date
     */
    private Object data;

}


