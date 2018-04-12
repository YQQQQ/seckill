package com.example.cloudfeign;

import net.sf.json.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "seckill-service")
public interface SeckillService {

    @RequestMapping(value = "/seckill-service/api/list",method = RequestMethod.GET)
    JSONObject goodsList();

    @RequestMapping(value = "/seckill-service/api/seckill" ,method = RequestMethod.POST)
    JSONObject execute(@RequestParam("seckillId") int seckillId, @RequestParam("userId")int userId, @RequestParam("userPhone")String userPhone, @RequestParam("address")String address, @RequestParam("md5")String md5);
}
