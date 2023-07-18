/**
 * @project dcloud-short-link
 * @description 创建宽表对象
 * @author Administrator
 * @date 2023/7/18 0018 21:07:35
 * @version 1.0
 */

package net.xdclass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkWideDO {

    /**
     * 短链压缩码
     */
    private String code;

    /**
     * 租户id
     */
    private Long accountNo;


    /**
     * 访问时间
     */
    private Long visitTime;

    /**
     * 站点来源，只记录域名
     */
    private String referer;


    /**
     * 1是新访客，0是老访客
     */
    private Integer isNew;


//==============DeviceInfoDO==================

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

    private  String ip;
}


