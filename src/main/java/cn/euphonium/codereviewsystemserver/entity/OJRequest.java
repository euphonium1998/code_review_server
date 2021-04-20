package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OJRequest {
    int qid;
    String code;
}
