package org.seckill.service;

import net.sf.json.JSONObject;
import org.seckill.dto.SeckillParam;
import org.seckill.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    JSONObject sreachUser(SeckillParam seckillParam);

    String login(String userName);

    int createUser(JSONObject jsonObject);

}
