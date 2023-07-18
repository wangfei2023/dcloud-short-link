/**
 * @project dcloud-short-link
 * @description 短链访问设备信息宽表
 * @author Administrator
 * @date 2023/7/18 0018 21:04:55
 * @version 1.0
 */

package net.xdclass.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xdclass.model.DeviceInfoDO;
import net.xdclass.model.ShortLinkWideDO;
import net.xdclass.util.DeviceUtil;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

public class DwmShortLinkWideApp {
    /**
     * DWM层 kafka相关配置
     */
    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";
    public static final String GROUP_ID = "dwm_short_link_group";


    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        //1、获取dwd日志
        FlinkKafkaConsumer<String> source = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(source);


        //2、格式进行转换 补齐设备信息
        SingleOutputStreamOperator<ShortLinkWideDO> deviceWideDS = ds.flatMap(new FlatMapFunction<String, ShortLinkWideDO>() {

            @Override
            public void flatMap(String value, Collector<ShortLinkWideDO> out) throws Exception {
               //还原json
                JSONObject jsonObject = JSON.parseObject(value);
               String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
                DeviceInfoDO deviceInfoDO = DeviceUtil.getDeviceInfo(userAgent);
                //配置短链基本信息;
                ShortLinkWideDO shortLinkWideDO = ShortLinkWideDO.builder()

                        //短链访问基本信息
                        .visitTime(jsonObject.getLong("ts"))
                        .accountNo(jsonObject.getJSONObject("data").getLong("accountNo"))
                        .code(jsonObject.getString("bizId"))
                        .referer(jsonObject.getString("referer"))
                        .isNew(jsonObject.getInteger("is_new"))
                        .ip(jsonObject.getString("ip"))


                        //设备
                        .browserName(deviceInfoDO.getBrowserName())
                        .os(deviceInfoDO.getOs())
                        .osVersion(deviceInfoDO.getOsVersion())
                        .deviceType(deviceInfoDO.getDeviceType())
                        .deviceManufacturer(deviceInfoDO.getDeviceManufacturer())
                        .udid(deviceInfoDO.getUdid())

                        .build();

                out.collect(shortLinkWideDO);
            }
        });


        deviceWideDS.print("设备信息补全");

        env.execute();
    }
}


