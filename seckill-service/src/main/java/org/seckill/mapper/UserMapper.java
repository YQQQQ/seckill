package org.seckill.mapper;

import org.seckill.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "userMapper")
public interface UserMapper {

    List<User> userList();

    String login(String userName);

    int insertUser(User user);

    int updateUser (User user);

    int deleteUser(int userId);

    User selectByName(String userName);
}
