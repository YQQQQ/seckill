package com.example.cloudfeign;

import net.sf.json.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "seckill-service")
public interface SeckillService {

    @RequestMapping(value = "/seckill-service/api/list",method = RequestMethod.GET)
    JSONObject goodsList();
}
