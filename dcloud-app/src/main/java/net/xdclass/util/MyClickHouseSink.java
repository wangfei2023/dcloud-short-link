/**
 * @project dcloud-short-link
 * @description Sink输出ClickHouse
 * @author Administrator
 * @date 2023/7/25 0025 14:44:00
 * @version 1.0
 */

package net.xdclass.util;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ShortLinkVisitStatsDO;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class MyClickHouseSink {
    private static String CLICK_HOUSE_SERVER=null;
    static {
        Properties properties =new Properties();
        InputStream in = KafkaUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
           log.error("加载clickhouse配置文件失败");
        }
        //获取key对应的value;
        CLICK_HOUSE_SERVER=properties.getProperty("clickhouse.servers");
    }

    /**
     * 获取向Clickhouse中写入数据的SinkFunction
     *
     *
     * @param sql
     * @param <T>
     * @return
     */
    public static SinkFunction getJdbcSink(String sql) {
        /**
         *  8、输出Clickhouse
         *      `code` String,
         *     `referer` String,
         *     `is_new` UInt64,
         *     `account_no` UInt64,
         *     `province` String,
         *     `city` String,
         *     `ip` String,
         *     `browser_name` String,
         *     `os` String,
         *     `device_type` String,
         *     `pv` UInt64,
         *     `uv` UInt64,
         *     `start_time` DateTime,
         *     `end_time` DateTime,
         *     `ts` UInt64
         */
        SinkFunction<ShortLinkVisitStatsDO> sinkFunction = JdbcSink.sink(
                sql,
                //执行写入操,设置占位符
                new JdbcStatementBuilder<ShortLinkVisitStatsDO>() {
                    @Override
                    public void accept(PreparedStatement ps, ShortLinkVisitStatsDO obj) throws SQLException {
                        ps.setObject(1, obj.getCode());
                        ps.setObject(2, obj.getReferer());
                        ps.setObject(3, obj.getIsNew());
                        ps.setObject(4, obj.getAccountNo());
                        ps.setObject(5, obj.getProvince());
                        ps.setObject(6, obj.getCity());
                        ps.setObject(7, obj.getIp());

                        ps.setObject(8, obj.getBrowserName());
                        ps.setObject(9, obj.getOs());
                        ps.setObject(10, obj.getDeviceType());

                        ps.setObject(11, obj.getPv());
                        ps.setObject(12, obj.getUv());
                        ps.setObject(13, obj.getStartTime());
                        ps.setObject(14, obj.getEndTime());
                        ps.setObject(15, obj.getVisitTime());
                    }
                },
                //batchSize属性，执行批次大小，默认5000   批量满5条才能进行写入;
                new JdbcExecutionOptions.Builder().withBatchSize(5).build(),
                //连接配置相关
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl(CLICK_HOUSE_SERVER)
                        .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                        .withUsername("default")
                        .build()
        );
        return sinkFunction;
    }
}


