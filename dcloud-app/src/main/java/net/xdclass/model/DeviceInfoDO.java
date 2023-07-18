/**
 * @project dcloud-short-link
 * @description 设备信息对象
 * @author Administrator
 * @date 2023/7/18 0018 19:27:05
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
public class DeviceInfoDO {
    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 系统版本
     */
    private String osVersion;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备厂商
     */
    private String deviceManufacturer;


    /**
     * 用户唯一标识
     */
    private String udid;
}


