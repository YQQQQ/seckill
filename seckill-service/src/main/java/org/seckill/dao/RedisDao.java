package org.seckill.dao;


import org.seckill.entity.SeckillGoods;
import org.seckill.until.SerializeUntils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
public class RedisDao {

    @Autowired
    private  JedisPool jedisPool;

    public void setSeckill(SeckillGoods seckill) {

        Jedis jedis = jedisPool.getResource();
        String key = "seckillGoods:" + seckill.getSeckillId();
        byte[] bytes = SerializeUntils.serialize(seckill);
        jedis.set(key.getBytes(), bytes);
        jedis.close();

    }


    public SeckillGoods getSeckill(int seckillId) {
        Jedis jedis = jedisPool.getResource();
        String key = "seckillGoods:" + seckillId;
        byte[] bytes = jedis.get(key.getBytes());//对象存在redis中为二进制的
        if (bytes != null) {
            SeckillGoods seckill = (SeckillGoods) SerializeUntils.deSerialize(bytes);
            return seckill;
        }
        jedis.close();
        return null;
    }


    public void set(int userId, String value) {
        String key = "seckill:" + userId;
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        jedis.close();
    }

    public String get(int guid) {
        String key = "seckill:" + guid;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    public void setGoodsNumber(int seckillId, int number) {
        Jedis jedis = jedisPool.getResource();
        // 监视key
        String watchkeys = "watchkeys:" + seckillId;
        jedis.set(watchkeys, String.valueOf(number));
        jedis.close();
    }
}

