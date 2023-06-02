package net.xdclass.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

/**
* jedis连接池配置文件
* @param host 配置文件中redis连接host
* @param port 配置文件中redis连接port
* @param password 配置文件中redis连接password
**/
/*
 @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
 */
@Component
public class JedisConfig {
   @Bean
    public JedisPoolConfig jedisPoolConfig(
           @Value("${spring.redis.jedis.pool.max-total}")int maxActive,
           @Value("${spring.redis.jedis.pool.max-idle}")int maxIdle,
           @Value("${spring.redis.jedis.pool.min-idle}")int minIdle,
           @Value("${spring.redis.jedis.pool.max-wait}")long maxWaitMillis,
           @Value("${spring.redis.jedis.pool.testOnBorrow}")boolean testOnBorrow
           ){
       JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
       jedisPoolConfig.setMaxTotal(maxActive);
       jedisPoolConfig.setMaxIdle(maxIdle);
       jedisPoolConfig.setMinIdle(minIdle);
       jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
       jedisPoolConfig.setTestOnBorrow(testOnBorrow);
       return jedisPoolConfig;
   }
  @Bean
    public JedisPool jedisPool(
            @Value("${spring.redis.jedis.pool.host}") String host,
            @Value("${spring.redis.jedis.pool.password}") String password,
            @Value("${spring.redis.jedis.pool.port}") int port,
            @Value("${spring.redis.jedis.pool.timeOut}")int timeOut, JedisPoolConfig jedisPoolConfig
  ){
      if (StringUtils.isNotEmpty(password)){
          return new JedisPool(jedisPoolConfig,host,port,timeOut,password);
      }
      return new JedisPool(jedisPoolConfig,host,port,timeOut);
  }
}
