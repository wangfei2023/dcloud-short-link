/**
 * @project dcloud-short-link
 * @description 自定义函数
 * @author Administrator
 * @date 2023/7/20 0020 16:27:48
 * @version 1.0
 */

package net.xdclass.func;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.ShortLinkWideDO;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
@Slf4j
public class LocationMapFunction extends RichMapFunction<ShortLinkWideDO,String> {
    private CloseableHttpClient httpClient;

    /**
     * ip位置解析
     */
    private static final String IP_PARSE_URL = "https://restapi.amap.com/v3/ip?ip=%s&output=json&key=a71504beedbd521e136ac5b9cb6e76c9";
    @Override
    public void open(Configuration parameters) throws Exception {
        httpClient = createCustomHttpClient();
    }

    @Override
    public void close() throws Exception {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @Override
    public String map(ShortLinkWideDO value) throws Exception {

        String ip = value.getIp();
        String url = String.format(IP_PARSE_URL, ip);

        HttpGet httpGet = new HttpGet(url);
        //同步线程操作;
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                JSONObject locationObj = JSON.parseObject(result);

                log.info(result);
                String province = locationObj.getString("province");
                String city = locationObj.getString("city");
                value.setProvince(province);
                value.setCity(city);
                return JSON.toJSONString(value);
            }
        } catch (Exception e) {
            log.error("ip 解析错误,value={},msg={}", value, e.getMessage());
        }

        return JSON.toJSONString(value);
    }


    /**
     * 自定义连接
     *
     * @return
     */
    public CloseableHttpClient createCustomHttpClient() {


        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);


        //MaxPerRoute是对maxtotal的细分，每个主机的并发最大是300，route是指域名
        connectionManager.setDefaultMaxPerRoute(300);
        //设置连接池最大是500个连接
        connectionManager.setMaxTotal(500);

        /**
         * 只请求 xdclass.net,最大并发300
         *
         * 请求 xdclass.net,最大并发300
         * 请求 open1024.com,最大并发200
         *
         * //MaxtTotal=400 DefaultMaxPerRoute=200
         * //只连接到http://xdclass.net时，到这个主机的并发最多只有200；而不是400；
         * //而连接到http://xdclass.net 和 http://open1024.com时，到每个主机的并发最多只有200；
         * // 即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。
         *
         */

        RequestConfig requestConfig = RequestConfig.custom()
                //返回数据的超时时间
                .setSocketTimeout(20000)
                //连接上服务器的超时时间
                .setConnectTimeout(10000)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(1000)
                .build();

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();

        return closeableHttpClient;
    }
}


