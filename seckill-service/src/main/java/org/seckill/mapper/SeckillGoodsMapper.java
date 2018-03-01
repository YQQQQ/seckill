package org.seckill.mapper;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SeckillGoods;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component(value = "seckillGoodsMapper")
public interface SeckillGoodsMapper {

    //减库存
    int reduceNumber(@Param("seckillId")int seckillId, @Param("killTime")Date killTime);

    //根据id查询商品信息
    SeckillGoods queryById(int seckillId);

    //根据偏移量查询秒杀商品列表
    List<SeckillGoods> queryAll(@Param("offset")int offset, @Param("limit")int limit);

    //使用存储过程执行秒杀
    void killByProcedure(Map<String,Object> paramMap);
}
