package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlMsg extends Msg {
    String sqlError;
}
