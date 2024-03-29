/**
 * @project dcloud-short-link
 * @description User-Agent提取测试
 * @author Administrator
 * @date 2023/7/18 0018 19:13:46
 * @version 1.0
 */

package xdclass.net;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
@Slf4j
public class UtilTest {
    @Test
    public void testUserAgentUtil(){

        //browserName=Chrome,os=Android,manufacture=Google Inc.,deviceType=Mobile
        //String userAgentStr = "Mozilla/5.0 (Linux; Android 10; LIO-AN00 Build/HUAWEILIO-AN00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045713 Mobile Safari/537.36 MMWEBID/3189 MicroMessenger/8.0.11.1980(0x28000B51) Process/tools WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64";

        //browserName=Chrome,os=Mac OS X,manufacture=Apple Inc.,deviceType=Computer
        //String userAgentStr = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";

        //browserName=Chrome,os=Android,manufacture=Google Inc.,deviceType=Mobile
        String userAgentStr = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36 Edg/114.0.1823.82";


        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        String browserName = browser.getGroup().getName();
        String os = operatingSystem.getGroup().getName();
        String manufacture = operatingSystem.getManufacturer().getName();
        String deviceType = operatingSystem.getDeviceType().getName();


        System.out.println("browserName="+browserName+",os="+os+",manufacture="+manufacture+",deviceType="+deviceType);



    }

}


