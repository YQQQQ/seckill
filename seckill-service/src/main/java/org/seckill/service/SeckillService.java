package org.seckill.service;

import net.sf.json.JSONObject;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillException;

public interface SeckillService {
    // 查询所有秒杀商品
    JSONObject getSeckillList();

    // 查询单个秒杀记录
    JSONObject getById(int seckillId);

    // 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(int seckillId);

    // 执行秒杀操作
    SeckillExecution executeSeckill(int seckillId, int userId, String userPhone,String address,String md5)
            throws SeckillException, RepeatKillExeception, SeckillException;

    // 执行秒杀操作by存储过程
    SeckillExecution executeSeckillByProcedure(int seckillId, int userId,String userPhone,String address, String md5);
}
