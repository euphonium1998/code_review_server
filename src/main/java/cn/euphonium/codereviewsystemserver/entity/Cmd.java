package cn.euphonium.codereviewsystemserver.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Cmd {
    String[] args;
    String[] env;
    Map<String, Object>[] files;
    long cpuLimit;
    long clockLimit;
    long memoryLimit;
    int procLimit;
    boolean strictMemoryLimit;
    Map<String, Map<String, String>> copyIn;
    String[] copyOutCached;

    //增添默认项
    //用户需要初始化args, files[0].put("content", "yourInput"), copyIn, copyOutCached
    public Cmd() {
        this.cpuLimit = 5000000000L; //5s
        this.clockLimit = 2 * this.cpuLimit;
        this.memoryLimit = 104857600; //~=10mb
        this.procLimit = 50;
        this.strictMemoryLimit = false;
        this.env = new String[]{"PATH=/usr/bin:/bin"};
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        map1.put("content", "");
        map2.put("name", "stdout");
        map2.put("max", 10240);
        map3.put("name", "stderr");
        map3.put("max", 10240);
        Map<String, Object>[] files = new Map[3];
        files[0] = map1;
        files[1] = map2;
        files[2] = map3;
        this.files = files;
        this.copyIn = new HashMap<>();
    }
}
