package cn.euphonium.codereviewsystemserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CodeMsg extends Msg {
    String code;

    public CodeMsg() {}
    public CodeMsg(int status) {
        this.status = status;
    }
}
