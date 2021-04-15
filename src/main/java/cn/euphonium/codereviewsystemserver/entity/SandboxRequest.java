package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SandboxRequest {
    Cmd[] cmd;
}
