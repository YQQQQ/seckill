package com.example.cloudribbon.controller;

import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import net.sf.json.JSONObject;

@RestController
public class SeckillController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/list")
    public Object list() {
        Object obj = restTemplate.getForObject("http://SECKILL-SERVICE/seckill-service/api/list", Object.class);

        System.out.println(obj);
         JSONObject jsonObject = JSONObject.fromObject(obj);
        return jsonObject;
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object InfoById(int seckillId) {
        return restTemplate.getForObject("http://SECKILL-SERVICE/seckill-service/api/detail?seckillId=" + seckillId, Object.class);
    }

    @RequestMapping(value = "/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Object exposer(int seckillId) {
        return restTemplate.getForObject("http://SECKILL-SERVICE/seckill-service/api/exposer?seckillId=" + seckillId, Object.class);
    }

    @RequestMapping(value = "/seckill", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Object execute(int seckillId, int userId, String userPhone, String address, String md5) {
        return restTemplate.getForObject("http://SECKILL-SERVICE/seckill-service/api/seckill?seckillId=" + seckillId + "&userId=" + userId + "&userPhone=" + userPhone +
                "&address=" + address + "&md5=" + md5, Object.class);
    }
}
