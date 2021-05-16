package cn.euphonium.codereviewsystemserver.service;

import cn.euphonium.codereviewsystemserver.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User login(User user);
    User modifyPassword(User user);
}
