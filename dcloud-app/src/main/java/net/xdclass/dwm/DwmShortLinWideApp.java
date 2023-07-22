//package net.xdclass.dwm;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import net.xdclass.model.DeviceInfoDO;
//import net.xdclass.model.ShortLinkWideDO;
//import net.xdclass.util.DeviceUtil;
//import net.xdclass.util.KafkaUtil;
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
//import org.apache.flink.util.Collector;
//
///**
// * 小滴课堂,愿景：让技术不再难学
// *
// * @Description
// * @Author 二当家小D
// * @Remark 有问题直接联系我，源码-笔记-技术交流群
// * @Version 1.0
// **/
//
//public class DwmShortLinWideApp {
//
//    /**
//     * 定义source topic
//     */
//    public static final String SOURCE_TOPIC = "dwd_link_visit_topic";
//
//    /**
//     * 定义消费者组
//     */
//    public static final String GROUP_ID = "dwm_short_link_group";
//
//    public static void main(String [] args) throws Exception {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//
//        env.setParallelism(1);
//
//        //DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);
//
//        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);
//
//        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
//
//
//        //格式装换，补齐设备信息
//        SingleOutputStreamOperator<ShortLinkWideDO> deviceWideDS = ds.flatMap(new FlatMapFunction<String, ShortLinkWideDO>() {
//            @Override
//            public void flatMap(String value, Collector<ShortLinkWideDO> out) throws Exception {
//
//                //还原json
//                JSONObject jsonObject = JSON.parseObject(value);
//
//                String userAgent = jsonObject.getJSONObject("data").getString("user-agent");
//
//                DeviceInfoDO deviceInfoDO = DeviceUtil.getDeviceInfo(userAgent);
//
//                String udid = jsonObject.getString("udid");
//                deviceInfoDO.setUdid(udid);
//
//                //配置短链基本信息宽表
//                ShortLinkWideDO shortLinkWideDO = ShortLinkWideDO.builder()
//                        //短链访问基本信息
//                        .visitTime(jsonObject.getLong("ts"))
//                        .accountNo(jsonObject.getJSONObject("data").getLong("accountNo"))
//                        .code(jsonObject.getString("bizId"))
//                        .referer(jsonObject.getString("referer"))
//                        .isNew(jsonObject.getInteger("is_new"))
//                        .ip(jsonObject.getString("ip"))
//
//                        //设备信息补齐
//                        .browserName(deviceInfoDO.getBrowserName())
//                        .os(deviceInfoDO.getOs())
//                        .osVersion(deviceInfoDO.getOsVersion())
//                        .deviceType(deviceInfoDO.getDeviceType())
//                        .deviceManufacturer(deviceInfoDO.getDeviceManufacturer())
//                        .udid(deviceInfoDO.getUdid())
//
//                        .build();
//
//                out.collect(shortLinkWideDO);
//
//            }
//        });
//
//
//        deviceWideDS.print("设备信息宽表补齐");
//
//        env.execute();
//    }
//}
//package net.xdclass.dwm;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import net.xdclass.model.DeviceInfoDO;
//import net.xdclass.model.ShortLinkWideDO;
//import net.xdclass.util.DeviceUtil;
//import net.xdclass.util.KafkaUtil;
//import org.apache.flink.api.common.functions.FlatMapFunction;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
//import org.apache.flink.util.Collector;
//
///**
// * 小滴课堂,愿景：让技术不再难学
// *
// * @Description
// * @Author 二当家小D
// * @Remark 有问题直接联系我，源码-笔记-技术交流群
// * @Version 1.0
// **/

package net.xdclass.dwm;

import net.xdclass.func.AsyncLocationRequestFunction;
import net.xdclass.func.DeviceMapFunction;
import net.xdclass.func.LocationMapFunction;
import net.xdclass.model.ShortLinkWideDO;
import net.xdclass.util.KafkaUtil;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @Description
 * @Author
 * @Remark
 * @Version 1.0
 **/

public class DwmShortLinWideApp {

    /**
     * 定义source topic
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

   //    DataStream<String> ds =  env.socketTextStream("127.0.0.1",8888);

        // 1、获取流
        FlinkKafkaConsumer<String> kafkaConsumer = KafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP_ID);

        DataStreamSource<String> ds = env.addSource(kafkaConsumer);
//
        ds.print();
        //2、格式装换，补齐设备信息
        SingleOutputStreamOperator<ShortLinkWideDO> deviceWideDS = ds.map(new DeviceMapFunction());

        deviceWideDS.print("设备信息宽表补齐");


        //3、补齐地理位置信息
      SingleOutputStreamOperator<String> shortLinkWideDS  = deviceWideDS.map(new LocationMapFunction());

      //  SingleOutputStreamOperator<String> shortLinkWideDS = AsyncDataStream.unorderedWait(deviceWideDS, new AsyncLocationRequestFunction(), 1000, TimeUnit.MILLISECONDS, 200);

        shortLinkWideDS.print("地理位置信息宽表补齐");

        FlinkKafkaProducer<String> kafkaProducer = KafkaUtil.getKafkaProducer(SINK_TOPIC);

        //4、将sink写到dwm层，kafka存储
        shortLinkWideDS.addSink(kafkaProducer);

        env.execute();
    }
}
