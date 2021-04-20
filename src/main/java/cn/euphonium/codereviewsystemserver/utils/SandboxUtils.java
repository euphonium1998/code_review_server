package cn.euphonium.codereviewsystemserver.utils;

import cn.euphonium.codereviewsystemserver.entity.*;
import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class SandboxUtils {

    public static final String sandboxBaseUrl = "http://118.178.194.230:5050";

    public static SandboxResponse runAndParse(String reqJSON) {
        //post restful
        String url = sandboxBaseUrl + "/run";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(reqJSON, headers);
        String responseJSONStr = restTemplate.postForObject(url, request, String.class);
//        System.out.println(responseJSONStr);
        responseJSONStr = responseJSONStr.substring(1, responseJSONStr.length() - 1);
//        System.out.println(responseJSONStr);
        SandboxResponse sandboxResponse = JSON.parseObject(responseJSONStr, SandboxResponse.class);
        return sandboxResponse;
    }

    public static String compileCodeInSandbox(String content) throws Exception {
        Cmd cmd = new Cmd();
        String[] args = {"/usr/bin/gcc", "a.c", "-o", "a", "-std=c99"};
        cmd.setArgs(args);
        Map<String, Map<String, String>> copyIn = cmd.getCopyIn();
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("content", content);
        copyIn.put("a.c", codeMap);
        String[] copyOutCached = new String[]{"a"};
        cmd.setCopyOutCached(copyOutCached);
        SandboxRequest req = new SandboxRequest();
        Cmd[] cmds = new Cmd[1];
        cmds[0] = cmd;
        req.setCmd(cmds);
        String reqJSON = JSON.toJSONString(req);

        SandboxResponse sandboxResponse = runAndParse(reqJSON);

        if (!sandboxResponse.getStatus().equals("Accepted")) {
            throw new Exception("compile error");
        }

        return sandboxResponse.getFileIds().get("a");
    }

    public static void deleteFileByIdInSandbox(String fileId) {
        String url = sandboxBaseUrl + "/file/" + fileId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
//        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.DELETE,null,String.class);
//        System.out.println(result);
    }

    public static OJResponse onlineJudgeOneSample(Sample sample, String fileId, OJResponse ojResponse) throws Exception {
        Cmd cmd = new Cmd();
        String[] args = {"a"};
        cmd.setArgs(args);
        Map<String, Object>[] files = cmd.getFiles();
        files[0].put("content", sample.getInput());
        Map<String, Map<String, String>> copyIn = cmd.getCopyIn();
        Map<String, String> codeMap = new HashMap<>();
        codeMap.put("fileId", fileId);
        copyIn.put("a", codeMap);
        SandboxRequest req = new SandboxRequest();
        Cmd[] cmds = new Cmd[1];
        cmds[0] = cmd;
        req.setCmd(cmds);
        String reqJSON = JSON.toJSONString(req);

        SandboxResponse sandboxResponse = runAndParse(reqJSON);
        String stderr = sandboxResponse.getFiles().get("stderr");
        String stdout = sandboxResponse.getFiles().get("stdout");
        if (!sandboxResponse.getStatus().equals("Accepted")) {
            if (sandboxResponse.getStatus().equals("Non Zero Exit Status")) {
                ojResponse.setStatus(stderr);
            } else {
                ojResponse.setStatus(sandboxResponse.getStatus());
            }
        } else if (!stderr.equals("")) {
            ojResponse.setStatus("stderr[" + stderr + "]");
        } else if (!stdout.equals(sample.getOutput())) {
            ojResponse.setStatus("wrong output");
            ojResponse.setInput(sample.getInput());
            ojResponse.setRealOutput(stdout);
            ojResponse.setExpectedOutput(sample.getOutput());
        } else {
            ojResponse.setMemory(ojResponse.getMemory() + sandboxResponse.getMemory());
            ojResponse.setRunTime(ojResponse.getRunTime() + sandboxResponse.getRunTime());
        }
        return ojResponse;
    }

}
