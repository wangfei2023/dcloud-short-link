/**
 * @project dcloud-short-link
 * @description uv统计
 * @author Administrator
 * @date 2023/7/21 0021 22:33:11
 * @version 1.0
 */

package net.xdclass.dwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.xdclass.func.UniqueVisitorFilterFunction;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public class DwmUniqueVisitorApp {
    /**
     * 定义source topic
     */
    public static final String SOURCE_TOPIC = "dwm_link_visit_topic";


    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_unique_visitor_group";


    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_unique_visitor_topic";

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //获取数据流;
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        //数据转换
        SingleOutputStreamOperator<JSONObject> jsonDs = ds.map(jsonStr -> JSON.parseObject(jsonStr));
        //分组;
        KeyedStream<JSONObject, String> keyedStream = jsonDs.keyBy(new KeySelector<JSONObject, String>() {
            @Override
            public String getKey(JSONObject value) throws Exception {
                return value.getString("udid");
            }
        });
        //排重过滤
        SingleOutputStreamOperator<JSONObject> filterDs = keyedStream.filter(new UniqueVisitorFilterFunction());
        filterDs.print("独立访客");
        //转成字符串写入kafka
        SingleOutputStreamOperator<String> UniqueVisitorDs = filterDs.map(obj -> obj.toJSONString());

        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);
        UniqueVisitorDs.addSink(kafkaProducer);
    }
}


