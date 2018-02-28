package org.seckill.dao;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.apache.log4j.Logger;
import org.seckill.entity.SeckillGoods;
import org.seckill.entity.User;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private Logger logger = Logger.getLogger(RedisDao.class);

    private final JedisPool jedisPool;

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    private RuntimeSchema<SeckillGoods> schema = RuntimeSchema.createFrom(SeckillGoods.class);

    public  SeckillGoods getSeckill(int seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckillGoods:"+seckillId;
                //对象存在redis中为二进制的
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null){
                    //空对象
                    SeckillGoods seckillGoods = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckillGoods,schema);
                    return seckillGoods;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public String setSeckill(SeckillGoods seckill){
        //set Object(Seckill)->序列化->byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckillGoods:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;//一小时。超时时间
                String result = jedis.setex(key.getBytes(), timeout, bytes);//成功返回"ok"
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return null;
    }

    public void set(int userId, String value) {
        String key = "seckill:"+ userId;
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
}
