package cn.euphonium.codereviewsystemserver.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileContent extends Msg {
    String content;
    String name;
}
