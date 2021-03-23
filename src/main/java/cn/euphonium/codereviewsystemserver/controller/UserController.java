package cn.euphonium.codereviewsystemserver.controller;

import cn.euphonium.codereviewsystemserver.entity.User;
import cn.euphonium.codereviewsystemserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public List<User> findAll(){
        return userService.findAll();
    }

    @RequestMapping("/login")
    public User login(@RequestBody User user) {
        return userService.login(user);
    }

}