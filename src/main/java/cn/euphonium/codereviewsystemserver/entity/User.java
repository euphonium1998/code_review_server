package cn.euphonium.codereviewsystemserver.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends Msg {
    String account;
    String password;
    String identity;
    String name;

    public User(int status) {
        this.status = status;

    }

    public User() {

    }
}
