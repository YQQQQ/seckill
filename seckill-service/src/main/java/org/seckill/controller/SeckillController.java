package org.seckill.controller;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.dao.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillParam;
import org.seckill.dto.SeckillResult;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillExeception;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/seckill-service/api")
public class SeckillController {
    private final Logger logger = Logger.getLogger(SeckillController.class);

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisDao redisDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)//资源
    @ResponseBody
    public Object list() {
        //获取列表页
        JSONObject result = seckillService.getSeckillList();
        logger.info(result);
        return result;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(int seckillId) {
        JSONObject result = new JSONObject();
        //返回详情页
        if ((Integer) seckillId == null) {
            return result;
        }
        result = seckillService.getById(seckillId);
        return result;
    }

    @RequestMapping(value = "/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody//返回类型作为ajax输出，转换为json数据
    public SeckillResult<Exposer> exposer(int seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/seckill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object execute(int seckillId, int userId, String userPhone, String address, String md5) {

        JSONObject jsonObject = new JSONObject();
        String number = seckillService.getById(seckillId).getString("number");
        if (Integer.parseInt(number) > 0) {
            //是否有秒杀记录
          //  if (!redisDao.get(userId).equals(seckillId)) {
                try {
                    //存储过程调用
                    SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, userId, userPhone, address, md5);
                    jsonObject = JSONObject.fromObject(new SeckillResult<SeckillExecution>(true, execution));
                } catch (RepeatKillExeception e) {
                    SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
                    jsonObject = JSONObject.fromObject(new SeckillResult<SeckillExecution>(true, execution));
                } catch (SeckillException e) {
                    SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
                    jsonObject = JSONObject.fromObject(new SeckillResult<SeckillExecution>(true, execution));
                } catch (Exception e) {
                    SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
                    jsonObject = JSONObject.fromObject(new SeckillResult<SeckillExecution>(false, execution));
                }
               // redisDao.set(userId, String.valueOf(seckillId));
           // }
        }else{
            jsonObject = JSONObject.fromObject(new SeckillResult(false, "库存不足"));
        }
        return jsonObject;
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
