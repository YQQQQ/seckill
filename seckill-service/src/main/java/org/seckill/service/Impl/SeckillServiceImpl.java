package org.seckill.service.Impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.seckill.dao.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillParam;
import org.seckill.entity.SeckillGoods;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.mapper.SeckillGoodsMapper;
import org.seckill.mapper.SuccessKilledMapper;
import org.seckill.service.SeckillService;
import org.seckill.until.DateUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    private static Logger logger = Logger.getLogger(SeckillServiceImpl.class);

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private SuccessKilledMapper successKilledMapper;
    @Autowired
    private RedisDao redisDao;

    //盐值用于混肴md5字符串
    private String salt = "foIHhouUU)~@##U)Nog';AGK;+o)oihjiKG.";

    @Override
    public JSONObject getSeckillList() {
        JSONArray jsonArray = new JSONArray();
        JSONObject result = new JSONObject();


        List<SeckillGoods> list = seckillGoodsMapper.queryAll();
        for (SeckillGoods seckillGoods : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("seckillId", seckillGoods.getSeckillId());
            jsonObject.put("name", seckillGoods.getName());
            jsonObject.put("number", seckillGoods.getNumber());
            jsonObject.put("price",seckillGoods.getPrice());
            jsonObject.put("createTime", DateUntil.getStringFromDate(seckillGoods.getCreateTime()));
            jsonObject.put("startTime", DateUntil.getStringFromDate(seckillGoods.getStartTime()));
            jsonObject.put("endTime", DateUntil.getStringFromDate(seckillGoods.getStartTime()));
            jsonArray.add(jsonObject);
        }

        result.put("total", jsonArray.size());
        result.put("items", jsonArray);
        return result;
    }

    @Override
    public JSONObject getById(int seckillId) {
        SeckillGoods seckillGoods = seckillGoodsMapper.queryById(seckillId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillGoods.getSeckillId());
        jsonObject.put("name", seckillGoods.getName());
        jsonObject.put("number", seckillGoods.getNumber());
        jsonObject.put("price",seckillGoods.getPrice());
        jsonObject.put("createTime", DateUntil.getStringFromDate(seckillGoods.getCreateTime()));
        jsonObject.put("startTime", DateUntil.getStringFromDate(seckillGoods.getStartTime()));
        jsonObject.put("endTime", DateUntil.getStringFromDate(seckillGoods.getEndTime()));
        return jsonObject;
    }

    @Override
    public Exposer exportSeckillUrl(int seckillId) {
        //优化点：缓存优化
        //超时的基础上维护一致性
        //1.访问redis
        SeckillGoods seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.访问数据库
            seckill = seckillGoodsMapper.queryById(seckillId);
            if (seckill != null) {
                //3.放入redis
                redisDao.setSeckill(seckill);
                logger.info(redisDao.get(seckillId));
            } else {
                return new Exposer(false, seckillId);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId,
                    nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public SeckillExecution executeSeckill(int seckillId, int userId, String userPhone, String address, String md5) throws SeckillException, RepeatKillExeception, SeckillException {

        // CAS 乐观锁！！！！
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存+记录购买行为
        try {
            //记录购买行为
            int insertCount = successKilledMapper.insertSuccessKilled(seckillId, userId, userPhone, address);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillExeception("seckill repeated");
            } else {
                //减库存,热点商品竞争（高并发点）
                int updateCount = seckillGoodsMapper.reduceNumber(seckillId, new Date());
                if (updateCount <= 0) {
                    //没有更新到记录,秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功,commit

                    SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(seckillId, userId);


                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillExeception e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译器异常，转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillByProcedure(int seckillId, int userId, String userPhone, String address, String md5) {

        // 网址错误
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        //秒杀时间
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("userId", userId);
        map.put("userPhone", userPhone);
        map.put("address", address);
        map.put("killTime", killTime);
        map.put("result", null);
        //执行存储过程 先添加记录在减库存
        try {
            seckillGoodsMapper.killByProcedure(map);
            int result = MapUtils.getInteger(map, "result", -2);//result默认为-2
            if (result == 1) {
                //秒杀成功
                SuccessKilled sk = successKilledMapper.queryByIdWithSeckill(seckillId, userId);
                logger.info(sk);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
