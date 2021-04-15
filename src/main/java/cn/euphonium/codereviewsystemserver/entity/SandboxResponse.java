package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SandboxResponse {
    String status;
    int exitStatus;
    long time;
    long memory;
    long runTime;
    Map<String, String> files;
    Map<String, String> fileIds;
}
