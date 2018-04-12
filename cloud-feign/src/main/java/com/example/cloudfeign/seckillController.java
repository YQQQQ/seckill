package com.example.cloudfeign;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class seckillController {

    @Autowired
    SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(){
        JSONObject jsonObject = seckillService.goodsList();
        System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping(value = "/seckill", method = RequestMethod.POST)
    public Object execute(@RequestParam("seckillId") int seckillId, @RequestParam("userId")int userId, @RequestParam("userPhone")String userPhone, @RequestParam("address")String address, @RequestParam("md5")String md5){
        JSONObject jsonObject = seckillService.execute(seckillId,userId,userPhone,address,md5);
        System.out.println(jsonObject);
        return jsonObject;
    }
}
