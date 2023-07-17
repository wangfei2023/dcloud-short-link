/**
 * @project dcloud-short-link
 * @description 日志采集
 * @author Administrator
 * @date 2023/7/17 0017 17:45:11
 * @version 1.0
 */

package net.xdclass.service.impl;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.LogTypeEnum;
import net.xdclass.model.LogRecord;
import net.xdclass.service.LogService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LogServiceImpl  implements LogService {
    private  static final String TOPIC_NAME="ods_link_visit_topic";
    @Autowired
     private KafkaTemplate kafkaTemplate;
    @Override
    public void  recodeShortLinkLog(HttpServletRequest request, String shortLinkCode, Long accountNo) {
       //ip,；浏览器信息
        String ipAddr = CommonUtil.getIpAddr(request);
        Map<String, String> headerMap =   CommonUtil.getAllRequestHeader(request);
        HashMap<String, String> avaiableMap = new HashMap<>();
        avaiableMap.put("user-agent",headerMap.get("user-agent"));
        avaiableMap.put("referer",headerMap.get("referer"));
        avaiableMap.put("accountNo",String.valueOf(accountNo));
        LogRecord logRecord = LogRecord.builder().event(LogTypeEnum.SHORT_LINK_TYPE.name())
                .data(avaiableMap)
                .ip(ipAddr)
                .timeStamp(CommonUtil.getCurrentTimestamp())
                .bizid(shortLinkCode).build();
        String jsonLog = JsonUtil.obj2Json(logRecord);
        kafkaTemplate.send(TOPIC_NAME,jsonLog);

    }
}


