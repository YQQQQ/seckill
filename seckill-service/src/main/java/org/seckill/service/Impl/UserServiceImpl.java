package org.seckill.service.Impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.seckill.dto.SeckillParam;
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
    public JSONObject sreachUser(SeckillParam seckillParam) {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        int page = (seckillParam.getPage() != null) ? seckillParam.getPage() : 1;
        int size = (seckillParam.getSize() != null) ? seckillParam.getSize() : 4;
        int from = (page - 1) * size;
        int to = from + size;
        List<User> users = userMapper.userList();

        for (User user : users) {
            JSONObject object = new JSONObject();
            object.put("userId", user.getUserId());
            object.put("userName", user.getUserName());
            object.put("phone", user.getPhone());
            object.put("address", user.getAddress());
            jsonArray.add(object);
        }
        from = from > jsonArray.size() ? jsonArray.size() : from;
        to = to > jsonArray.size() ? jsonArray.size() : to;
        List resultArray = jsonArray.subList(from, to);
        result.put("total", jsonArray.size());
        result.put("items", resultArray);
        return result;
    }

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
