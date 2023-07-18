/**
 * @project dcloud-short-link
 * @description
 * @author Administrator
 * @date 2023/7/18 0018 13:53:36
 * @version 1.0
 */

package net.xdclass.func;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;

import java.util.concurrent.TimeUnit;
@Slf4j
public class VistorMapFunction extends RichMapFunction<JSONObject,String> {
    //记录用户的udid访问
    //记录用户udid访问状态
    private ValueState<String> newDayVisitorState;;

    private static final String NEWVISTOR="新用户访问";
    private static final String OLDVISTOR="老用户访问";
    @Override
    public void open(Configuration parameters) throws Exception {
        //对状态进行初始化;判断一下是不是新用户访问;
        newDayVisitorState = getRuntimeContext().getState(new ValueStateDescriptor<String>("newDayVisitorState", String.class));
    }


    @Override
    public String map(JSONObject value) throws Exception {
        //获取之前是否有访问日期;
        String beForeDateState = newDayVisitorState.value();
        System.out.println(beForeDateState);
        //获取当前访问的时间戳;
        Long stamp = value.getLong("ts");
        String currentDateStr = TimeUtil.format(stamp);
        //判断日期是否为空判断新老客户访问;(//TODO:老访客;)
        if (StringUtils.isNotBlank(beForeDateState)){
            //判断是否为同一天数据，一样则是老访客
            if (beForeDateState.equals(currentDateStr)) {
                value.put("is_new", OLDVISTOR);
                System.out.println("老访客 "+currentDateStr);
            }
        }else{
            //TODO:新访客;

            value.put("is_new",NEWVISTOR);
            newDayVisitorState.update(currentDateStr);
            log.info("新访客：{}",currentDateStr);
        }
        return value.toString();
    }
}


