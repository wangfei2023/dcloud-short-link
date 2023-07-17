/**
 * @project dcloud-short-link
 * @description 入口类编写
 * @author Administrator
 * @date 2023/7/17 0017 23:38:06
 * @version 1.0
 */

package net.xdclass.dwd;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

@Slf4j
public class DwdShortLinkLogApp {
    //定义TOPIC名称
    public static final String SOURCE_TOPIC="ods_link_visit_topic";
    //定义消费者组
    public static final String GROUP_ID="dwd_short_link_group";
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//        DataStream<String> ds = env.socketTextStream("127.0.0.1", 8888);
        //监听kafka;
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        ds.print();
        env.execute();
    }
}


