package org.seckill.controller;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seckill-service/api")
public class UserController {
    private static Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Object login(String userName, String password) {
        JSONObject result = new JSONObject();
        int code = 0;
        String pwd = userService.login(userName);
        try {
            if (!pwd.equals(password)){
                code = -1;
            }else code = 200;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        logger.info(userName +":"+ password);
        result.put("code",code);
        return result;
    }


    @RequestMapping("/user")
    @ResponseBody
    public Object selectByName(String userName) {
        long startTime = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            result = userService.selectByName(userName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        result.put("cost", (System.currentTimeMillis() - startTime) + "ms");
        logger.info( result.toString());
        return result;
    }
}
