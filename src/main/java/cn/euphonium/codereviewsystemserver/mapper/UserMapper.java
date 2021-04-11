package cn.euphonium.codereviewsystemserver.mapper;

import cn.euphonium.codereviewsystemserver.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();

    User login(User user);

    String getNameByAccount(String account);
}
