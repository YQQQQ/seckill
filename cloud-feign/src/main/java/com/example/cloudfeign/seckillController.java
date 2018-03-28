package com.example.cloudfeign;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
