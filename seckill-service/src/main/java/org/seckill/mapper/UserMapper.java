package org.seckill.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.seckill.entity.User;
import java.util.List;

@Mapper
public interface UserMapper {

    List<User> userList();

    String login(String userName);

    int insertUser(User user);

    int updateUser (User user);

    int deleteUser(int userId);
}
