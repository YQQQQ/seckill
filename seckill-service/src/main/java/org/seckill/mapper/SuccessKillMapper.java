package org.seckill.mapper;


import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;
import org.springframework.stereotype.Component;

@Component(value = "successKillMapper")
public interface SuccessKillMapper {
    //插入购买明细，可过滤重复（联合主键）
    int insertSuccessKilled(@Param("seckillId")int seckillId, @Param("userId")int userId,@Param("userPhone") String userPhone,@Param("address") String address);

    //根据id查询SuccessKilled并携带Seckill实体
    JSONObject queryByIdWithSeckill(@Param("seckillId")int seckillId, @Param("userId")int userId);

}
