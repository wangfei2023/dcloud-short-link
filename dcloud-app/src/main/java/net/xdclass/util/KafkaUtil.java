/**
 * @project dcloud-short-link
 * @description Kafka工具类封装
 * @author Administrator
 * @date 2023/7/18 0018 00:11:31
 * @version 1.0
 */

package net.xdclass.util;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaUtil {
    /**
     * ODS层 kafka相关配置
     */
    public static final String SOURCE_TOPIC = "ods_link_visit_topic";
    public static final String GROUP_ID = "dwd_short_link_group";
    /**
     * kafka broker地址
     */
    private static String KAFKA_SERVER = null;

    static{
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = KafkaUtil.class.getClassLoader().getResourceAsStream("application.properties");
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取key对应的value值
        KAFKA_SERVER = properties.getProperty("kafka.servers");
    }

    /**
     * 获取flink kafka消费者
     * @param topic
     * @param groupId
     * @return
     */
    public static FlinkKafkaConsumer<String> getKafkaConsumer(String topic, String groupId) {
        //Kafka连接配置
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        return new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);
    }


    /**
     * 获取flink kafka生产者
     * @param topic
     * @return
     */
    public static FlinkKafkaProducer<String> getKafkaProducer(String topic) {
        return new FlinkKafkaProducer<>(KAFKA_SERVER, topic, new SimpleStringSchema());
    }
}


