/**
 * @project dcloud-short-link
 * @description 设备工具类
 * @author Administrator
 * @date 2023/7/18 0018 11:17:16
 * @version 1.0
 */

package net.xdclass.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import net.xdclass.model.DeviceInfoDO;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.util.Map;

public class DeviceUtil {
    /**
     * 生成web设备唯一ID
     * @param map
     * @return
     */
    public static String geneWebUniqueDeviceId(Map<String,String> map){
        String deviceId = MD5(map.toString());
        return deviceId;
    }



    /**
     * MD5加密
     *
     * @param data
     * @return
     */
    public static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;

    }

    /**
     * 获取浏览器对象
     * @param request
     * @return
     */
    private static Browser getBrowser(String agent) {
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();
        return browser;
    }
    /**
     * 获取操作系统对象
     * @param userAgent
     * @return
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem operatingSystem = agent.getOperatingSystem();
        return operatingSystem;
    }

    /**
     *
     *
     * 浏览器名称
     * @param userAgent
     * @return  Firefox、Chrome
     */
    public static String getBrowserName(String userAgent) {
        Browser browser =  getBrowser(userAgent);
        String browserGroup = browser.getGroup().getName();
        return browserGroup;
    }

    /**
     * 获取deviceType//todo:设备类型;
     * @param userAgent
     *
     * @return  MOBILE、COMPUTER
     */
    public static String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem =  getOperatingSystem(userAgent);
        String deviceType = operatingSystem.getDeviceType().toString();
        return deviceType;
    }


    /**
     * 获取os：Windows/ios/Android
     * @param userAgent
     * @return 操作系统;
     */
    public static String getOS(String userAgent) {
        OperatingSystem operatingSystem =  getOperatingSystem(userAgent);
        String os = operatingSystem.getGroup().getName();
        return os;
    }


    /**
     * 获取device的生产厂家
     *
     * @param userAgent
     * @return GOOGLE、APPLE
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem =  getOperatingSystem(userAgent);

        String deviceManufacturer = operatingSystem.getManufacturer().toString();
        return deviceManufacturer;
    }
    /**
     * 操作系统版本
     * @param userAgent
     * @return Android 1.x、Intel Mac OS X 10.15
     */
    public static String getOSVersion(String userAgent) {
        String osVersion = "";
        if(StringUtils.isBlank(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf("(")+1,
                userAgent.indexOf(")")).split(";");
        if(null == strArr || strArr.length == 0) {
            return osVersion;
        }

        osVersion = strArr[1];
        return osVersion;
    }
  /**
   * @description TODO
   * 解析对象
   * @return
   * @author
   * @date
   */
   public static DeviceInfoDO getDeviceInfo(String agent){
       DeviceInfoDO deviceInfoDO = DeviceInfoDO.builder().browserName(getBrowserName(agent))
               .deviceManufacturer(getDeviceManufacturer(agent))
               .deviceType(getDeviceType(agent))
               .os(getOS(agent))
               .osVersion(getOS(agent))
               .build();
       return deviceInfoDO;

   }

}


