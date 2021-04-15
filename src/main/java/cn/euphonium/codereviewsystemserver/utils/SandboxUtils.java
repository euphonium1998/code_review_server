package cn.euphonium.codereviewsystemserver.utils;

import cn.euphonium.codereviewsystemserver.entity.Cmd;
import cn.euphonium.codereviewsystemserver.entity.SandboxRequest;
import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class SandboxUtils {

    public static String compileCodeInSandbox(String content, String name) {
        Cmd cmd = new Cmd();
        String[] args = {"/usr/bin/gcc", "a.c", "-o", "a", "-std=c99"};
        cmd.setArgs(args);
        Map<String, Object>[] files = cmd.getFiles();
        files[0].put("content", "test");
        SandboxRequest req = new SandboxRequest();
        Cmd[] cmds = new Cmd[1];
        cmds[0] = cmd;
        req.setCmd(cmds);
        String reqJSON = JSON.toJSONString(req);
        return reqJSON;
    }
}
