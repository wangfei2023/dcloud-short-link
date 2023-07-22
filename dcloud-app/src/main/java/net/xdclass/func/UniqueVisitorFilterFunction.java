/**
 * @project dcloud-short-link
 * @description 判断访客uv;
 * @author Administrator
 * @date 2023/7/22 0022 10:51:42
 * @version 1.0
 */

package net.xdclass.func;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

import java.util.Date;
@Slf4j
//RichFilterFunction存储初始化;
public class UniqueVisitorFilterFunction extends RichFilterFunction<JSONObject> {
    private ValueState<String> lastVisitDateState=null;
    @Override
    public void open(Configuration parameters) throws Exception {
        //初始状态;
        ValueStateDescriptor<String> lastVisitDateStateDes = new ValueStateDescriptor<>("visitDateState", String.class);
        //统计uv;
        StateTtlConfig stateTtlConfig = StateTtlConfig.newBuilder(Time.days(1)).build();
        lastVisitDateStateDes.enableTimeToLive(stateTtlConfig);
        //存储访问状态;
        this.lastVisitDateState = getRuntimeContext().getState(lastVisitDateStateDes);
    }
    @Override
    public boolean filter(JSONObject jsonObj) throws Exception {
        //获取当前访问时间;
        Long visitTime = jsonObj.getLong("visitTime");
        String udid = jsonObj.getString("udid");
        //当前访问时间;
        String currentVisitDate = TimeUtil.format(visitTime);
        //获取上次的状态访问时间
        String lastVisitDate = lastVisitDateState.value();
        if (StringUtils.isNotBlank(lastVisitDate)&&currentVisitDate.equalsIgnoreCase(lastVisitDate)){
           log.info(udid+"已经在"+currentVisitDate+"访问过");
           return false;
        }else{
            log.info(udid+"在"+currentVisitDate+"初次访问");
            lastVisitDateState.update(currentVisitDate);
            return true;
        }
    }



    @Override
    public void close() throws Exception {
        super.close();
    }
}


