/**
 * @project dcloud-short-link
 * @description 入口类编写
 * @author Administrator
 * @date 2023/7/17 0017 23:38:06
 * @version 1.0
 */

package net.xdclass.dwd;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.func.VistorMapFunction;
import net.xdclass.util.DeviceUtil;
import net.xdclass.util.KafkaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DwdShortLinkLogApp {
    //定义 source TOPIC名称（在kafka进行创建topic）
    public static final String SOURCE_TOPIC="ods_link_visit_topic";


    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_unique_visitor_group";

    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_unique_visitor_topic";
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, // 一个时间段内的最大失败次数
                Time.of(5, TimeUnit.MINUTES), // 衡量失败次数的是时间段
                Time.of(10, TimeUnit.SECONDS) // 间隔
        ));

      env.setParallelism(1);

        DataStream<String> ds = env.socketTextStream("127.0.0.1", 8888);
        //监听kafka;
//        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
//        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        ds.print();
        SingleOutputStreamOperator<JSONObject> jsonDs = ds.flatMap(new FlatMapFunction<String, JSONObject>() {
            @Override
            public void flatMap(String value, Collector<JSONObject> out) throws Exception {

                JSONObject jsonObject = JSONObject.parseObject(value);
                //生成唯一的id;
                String udid=getDeviceId(jsonObject);
                String referer = getReferer(jsonObject);
                jsonObject.put("udid",udid);
                jsonObject.put("referer",referer);
                out.collect(jsonObject);

            }
        });
        //分组
        KeyedStream<JSONObject, String> keyedStream = jsonDs.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject value) throws Exception {
                return value.getString("udid");
            }
        });
        //识别
        SingleOutputStreamOperator<String> jsonDSWithVistorState = keyedStream.map(new VistorMapFunction());
        jsonDSWithVistorState.print("ods新老客户访问");
        //存储到dwd;(输出到kafka)
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);
        jsonDSWithVistorState.addSink(kafkaProducer);

        env.execute();
    }

    /**
     * @description TODO
     * 提取referer
     * @return
     * @author
     * @date
     */
    public static String getReferer(JSONObject jsonObject){
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
         if (dataJsonObject.containsKey("referer")){
             String referer = dataJsonObject.getString("referer");
             if (StringUtils.isNotBlank(referer)){
                 try {
                     URL url = new URL(referer);
                     return url.getHost();
                 } catch (MalformedURLException e) {
                    log.info("referer提取失败：{}",e);
                 }
             }

         }
        return "";
    }
    /**
     * @description TODO
     * 生成设备的id
     * @return
     * @author
     * @date
     */
    public static String getDeviceId(JSONObject jsonObject){
            TreeMap<String, String> map = new TreeMap<>();
        try {
            map.put("ip",jsonObject.getString("ip"));
            map.put("event",jsonObject.getString("event"));
            map.put("bizId",jsonObject.getString("bizId"));
            String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
            map.put("userAgent",userAgent);
            String deviceId = DeviceUtil.geneWebUniqueDeviceId(map);
            return  deviceId;
        } catch (Exception e) {
            log.error("生产唯一deviceId异常：{}", jsonObject);
            return null;
        }
    }
}


