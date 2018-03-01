package org.seckill.controller;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.dto.SeckillParam;
import org.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seckill-service/api")
public class UserController {
    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public Object login(String userName,String password) {
        JSONObject result = new JSONObject();
        int code = 0;
        try {
            if (!password.equals(userService.login(userName))){
                code = -1;
            }else code = 1;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        result.put("state",code);
        return result;
    }

    @RequestMapping("/userList")
    @ResponseBody
    public Object sreachUser(SeckillParam seckillParam) {
        long startTime = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            result = userService.sreachUser(seckillParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        result.put("cost", (System.currentTimeMillis() - startTime) + "ms");
        logger.info(seckillParam.toString() + "---->" + result.toString());
        return result;
    }
}
