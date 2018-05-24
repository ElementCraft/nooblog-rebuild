package com.noobug.NooblogRebuild.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class RedisUtils {
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private Jedis conn;

    /**
     * 关闭连接
     */
    public void close() {
        if (conn != null) conn.close();
    }

    /**
     * 获取Jedis连接的实例
     *
     * @return jedis实例
     */
    public Jedis getConnection() {
        if (conn == null || !conn.isConnected())
            conn = jedisConnectionFactory.getShardInfo().createResource();

        return conn;
    }

}
