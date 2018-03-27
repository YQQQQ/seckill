package org.seckill.service.Impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.entity.User;
import org.seckill.mapper.UserMapper;
import org.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;


    @Override
    public String login(String userName) {
        return userMapper.login(userName);
    }

    @Override
    public int createUser(JSONObject jsonObject) {
        User user = new User();
        user.setUserName(jsonObject.getString("name"));
        user.setPhone(jsonObject.getString("phone"));
        user.setAddress(jsonObject.getString("address"));
        user.setPassword(jsonObject.getString("password"));

        return userMapper.insertUser(user);
    }

    @Override
    public JSONObject selectByName(String userName) {
        User user = userMapper.selectByName(userName);
        JSONObject object = new JSONObject();
        object.put("userId", user.getUserId());
        object.put("userName", user.getUserName());
        object.put("phone", user.getPhone());
        object.put("address", user.getAddress());
        logger.info(user);
        return object;
    }


}
