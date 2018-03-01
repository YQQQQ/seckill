package org.seckill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author yecy
 * @date 2018-01-22 20:15
 */
@Configuration
@PropertySource("classpath:config/config.properties")
public class RedisConfig {

    @Value("${redis.config.maxIdle}")
    private int maxIdle;

    @Value("${redis.config.maxTotal}")
    private int maxTotal;

    @Value("${redis.config.maxWaitMillis}")
    private long maxWaitMillis;

    @Value("${redis.config.host}")
    private String host;

    @Value("${redis.config.port}")
    private int port;

    @Value("${redis.config.timeout}")
    private int timeout;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return new JedisPool(jedisPoolConfig, host, port, timeout);
    }

}
