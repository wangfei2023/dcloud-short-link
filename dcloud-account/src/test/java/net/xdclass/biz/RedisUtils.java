package net.xdclass.biz;


import net.xdclass.config.JedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



public class RedisUtils {
 @Autowired
   private JedisConfig  jedisConfig;
    private  static Logger logger=LoggerFactory.getLogger(RedisUtils.class);

    //todo:获取key;
    public   String get(String key){
        JedisPoolConfig jedisPoolConfig = jedisConfig.jedisPoolConfig(100, 50, 10, 60000, true);
        JedisPool jedisPool = jedisConfig.jedisPool("124.222.215.26", "xdclass.net", 6379, 2000, jedisPoolConfig);
        logger.info("redis获取key{}",key);
        Jedis jedis = jedisPool.getResource();
        return jedis.get(key);
    }
//    public  void set(String key,String value){
//        Jedis jedis = jedisPool.getResource();
//        logger.info("redis写入key,value{}",key,value);
//        jedis.hset("myhash", "field1t666666", "value1");
//        jedis.close();
//    }
//    public   void set1(String key, String value, long timeOut, TimeUnit timeUnit){
//        Jedis jedis = jedisPool.getResource();
//        logger.info("redis写入key,value{}",key,value);
//        jedis.set(key,value);
//    }
//    public  void del(String key){
//        Jedis jedis = jedisPool.getResource();
//        logger.info("redis删除数据key{}",key);
//        Long deletedKeys = jedis.del(key);
//        // 根据返回值判断是否删除成功
//        if (deletedKeys > 0) {
//            System.out.println("成功删除键：" + key);
//        } else {
//            System.out.println("键不存在或删除失败：" + key);
//        }
//    }
}
