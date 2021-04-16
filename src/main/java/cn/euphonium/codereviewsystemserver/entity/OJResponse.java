package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OJResponse {
    String status;
    long memory;
    long runTime;
    String input;
    String realOutput;
    String expectedOutput;
}
