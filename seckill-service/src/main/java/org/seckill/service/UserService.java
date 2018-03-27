package org.seckill.service;

import net.sf.json.JSONObject;

public interface UserService {

    String login(String userName);

    int createUser(JSONObject jsonObject);

    JSONObject selectByName(String userName);

}
