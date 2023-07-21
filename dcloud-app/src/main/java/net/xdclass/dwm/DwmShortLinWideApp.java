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
import net.xdclass.func.AsyncLocationRequestFunction;
import net.xdclass.func.DeviceMapFunction;
import net.xdclass.func.LocationMapFunction;
import net.xdclass.model.DeviceInfoDO;
import net.xdclass.model.ShortLinkWideDO;
import net.xdclass.util.DeviceUtil;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class DwmShortLinWideApp {

    /**
     * 定义source topic（在kafka进行创建topic）
     */
    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";

    /**
     * 定义消费者组
     */
    public static final String GROUP_ID = "dwm_short_link_group";


    /**
     * 定义输出
     */
    public static final String SINK_TOPIC = "dwm_link_visit_topic";

    public static void main(String [] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

     //DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);
       //获取流
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);

        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
        ds.print();

        //2格式装换，补齐设备信息
       SingleOutputStreamOperator<ShortLinkWideDO> deviceWideDS = ds.map(new DeviceMapFunction());

        deviceWideDS.print("设备信息宽表补齐");

        //补齐地址位置;
      // SingleOutputStreamOperator<String> shortLinkWideDS = deviceWideDS.map(new LocationMapFunction());
        SingleOutputStreamOperator<String> shortLinkWideDS  = AsyncDataStream.unorderedWait(deviceWideDS, new AsyncLocationRequestFunction(), 1000, TimeUnit.MILLISECONDS, 100);
        shortLinkWideDS.print("地理位置信息表的补齐");
        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);
        //将sink写到dwm中,kafka进行存储;
        shortLinkWideDS.addSink(kafkaProducer);
        env.execute();
    }
}



