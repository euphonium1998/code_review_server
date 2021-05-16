package cn.euphonium.codereviewsystemserver.service.impl;

import cn.euphonium.codereviewsystemserver.entity.ConstInfo;
import cn.euphonium.codereviewsystemserver.entity.User;
import cn.euphonium.codereviewsystemserver.mapper.UserMapper;
import cn.euphonium.codereviewsystemserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User login(User user) {
        User userGot = userMapper.login(user);
        return userGot == null ? new User(ConstInfo.ERROR) : userGot;
    }

    @Override
    public User modifyPassword(User user) {
        int flag = userMapper.modifyPassword(user);
        if (flag == 1) {
            return userMapper.login(user);
        } else {
            return new User(ConstInfo.ERROR);
        }
    }


}
