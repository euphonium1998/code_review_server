package cn.euphonium.codereviewsystemserver;

import cn.euphonium.codereviewsystemserver.entity.*;
import cn.euphonium.codereviewsystemserver.mapper.QuestionMapper;
import cn.euphonium.codereviewsystemserver.mapper.UserMapper;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import cn.euphonium.codereviewsystemserver.utils.CodeUtils;
import cn.euphonium.codereviewsystemserver.utils.SandboxUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@SpringBootTest
class CodeReviewSystemServerApplicationTests {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        String test = "/* example */\n" +
                "#include<stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "    printf(\"hello world\\n\");\n" +
                "}";
        System.out.println(CodeUtils.codeFormat(test));

//        System.out.println(CodeUtils.codeFormat(test));
//        System.out.println(test);
    }

    @Test
    void quote() {
        String s = "printf(\'fdsfa\', \'dsfe\')";
        List<String> list = new ArrayList<>();
        String res = CodeUtils.quotesReplacement('\'', list, s);
        System.out.println(res);
        for (String s1 : list) {
            System.out.println(s1);
        }
    }

    @Test
    void error1() {
        String s = "#include<stdio.h>\n" +
                "int main() { //code he\"\"re1\n" +
                "    int a=0, b =1;\n" +
                "    printf(\"hellon\");\n" +
                "    return 0;\n" +
                "    for (int i = 0; i <3; i++) { //code3\n" +
                "        b+=i;\n" +
                "        \n" +
                "    }\n" +
                "    for (int k = 0; k < 22;) {\n" +
                "        k<< 1;\n" +
                "    }\n" +
                "    /*\n" +
                "    code he're'2\n" +
                "    */\n" +
                "}";
        System.out.println(CodeUtils.codeFormat(s));
    }

    @Test
    void error2() {
        String s = "    for (int k = 0; k < 22;) {\n";
        String s2 = "printf(\"hello world\\n\")";
//        System.out.println(CodeUtils.formatMain(s));
        System.out.println(CodeUtils.formatMain(s2));
    }

    @Test
    void operator() {
        String s = "b+=i";
        CodeUtils.operatorProcess(s);
    }

    @Test
    void splint() {
        String s = "aa.c: (in function main)\n" +
                "aa.c:8:2: Path with no return in function declared to return int\n" +
                "  There is a path through a function declared to return a value on which there\n" +
                "  is no return statement. This means the execution may fall through without\n" +
                "  returning a meaningful result to the caller. (Use -noret to inhibit warning)\n" +
                "aa.c:8:2: Fresh storage a not released before return\n" +
                "  A memory leak has been detected. Storage allocated locally is not released\n" +
                "  before the last reference to it is lost. (Use -mustfreefresh to inhibit\n" +
                "  warning)\n" +
                "   aa.c:6:37: Fresh storage a created\n" +
                "aa.c:5:6: Variable c declared but not used\n" +
                "  A variable is declared but never used. Use /*@unused@*/ in front of\n" +
                "  declaration to suppress message. (Use -varuse to inhibit warning)\n" +
                "aa.c:6:7: Variable a declared but not used";
        System.out.println(CodeUtils.splintProcess(s));
    }

//    @Test
//    void insertQ() {
//
//        Question q = new Question();
//        q.setName("test test");
//        q.setAccount("170110317");
//        q.setQuestionDescription("alielie");
//        q.setInputDescription("bilibili");
//        q.setOutputDescription("acfun");
//        q.setSampleInput("1 2 3\n" +
//                "4 5 6 7\n" +
//                "2 3");
//        q.setSampleOutput("2 3 4");
//        int i = questionMapper.insertOneQuestion(q);
//        System.out.println(i);
//    }
//
//    @Test
//    void selectAllQ() {
//        List<Question> q = questionMapper.selectAllQuestion();
//        System.out.println(q);
//    }
//
//    @Test
//    void insertOneSample() {
//        Sample s = new Sample();
//        s.setPid(1);
//        s.setInput("3 5");
//        s.setOutput("8");
//        questionMapper.insertOneSample(s);
//    }
//
//    @Test
//    void selectOneQuestion() {
//        Question q = questionMapper.selectOneQuestion(1);
//        System.out.println(q);
//    }
//
//    @Test
//    void testTransactional() {
//        try {
//            questionService.insertOneQuestion(new Question());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("false");
//        }
//    }
//
//    @Test
//    void testGetName() {
//        System.out.println(userMapper.getNameByAccount("170110314"));
//    }
//
//    @Test
//    void selectOneQuestionById() {
//        System.out.println(questionService.selectOneQuestionById(1));
//    }
//
//    @Test
//    void restTemplate() {
//        String url = "http://47.97.202.211:16666/findAll";
//
//        RestTemplate restTemplate = new RestTemplate();
//        String s = restTemplate.getForObject(url, String.class);
//        System.out.println(s);
//    }
//
//    @Test
//    void testInt() {
//        long i = 30000000000L;
//        System.out.println(i);
//    }

//    @Test
//    void restTemplatePost() {
//        String url = "http://118.178.194.230:5050/file";
//        RestTemplate restTemplate = new RestTemplate();
//        String s = restTemplate.getForObject(url, String.class);
//        System.out.println(s);
//    }
//
//    @Test
//    void testFastJson() throws Exception {
//        String s = SandboxUtils.compileCodeInSandbox("hh");
//        System.out.println(s);
//    }
//
//    @Test
//    void testFastJSON2() {
//        String s = "[{\"status\":\"Accepted\",\"exitStatus\":0,\"time\":1285585,\"memory\":262144,\"runTime\":1788296,\"files\":{\"stderr\":\"\",\"stdout\":\"20\\n\"}}]";
//        s = s.substring(1, s.length() - 1);
//        System.out.println(s);
//        SandboxResponse res = JSON.parseObject(s, SandboxResponse.class);
//        System.out.println(res);
//    }
//
//    @Test
//    void testPost() throws Exception {
//        String s = "#include<stdio.h>\nint main() {\nint a, b;\nscanf(\"%d %d\", &a, &b);\nprintf(\"%d\", a - b);\n}";
////        System.out.println(s);
//        String res = SandboxUtils.compileCodeInSandbox(s);
//        System.out.println(res);
//    }
//
//    @Test
//    void testDelete() {
//        try {
//            SandboxUtils.deleteFileByIdInSandbox("ABNZVTDQPIUQBDHN");
//        } catch (Exception e) {
//            System.out.println(e);
//            System.out.println(e.toString());
////            throw e;
//        }
//    }
//
//    @Test
//    void testOJ() {
//        String s = "#include<stdio.h>\nint main() {\nint a, b;\nscanf(\"%d %d\", &a, &b);\nprintf(\"%d\", a + b);\nwhile(1){}}";
//        Question q = new Question();
//        List<Sample> samples = new ArrayList<>();
//        Sample sample1 = new Sample();
//        Sample sample2 = new Sample();
//        Sample sample3 = new Sample();
//        sample1.setInput("1 2");
//        sample2.setInput("23 54");
//        sample3.setInput("2 4");
//        sample1.setOutput("3");
//        sample2.setOutput("77");
//        sample3.setOutput("5");
//        samples.add(sample1);
//        samples.add(sample2);
//        samples.add(sample3);
//        q.setSamples(samples);
//        OJResponse  res = questionService.onlineJudgement(q, s);
//        System.out.println(res);
//
//    }

    @Test
    void selectQuestionByName() {
        System.out.println(questionMapper.selectQuestionByName("a+b问题"));
    }

    @Test
    void nullPointerTest() {
        OJRequest ojRequest = new OJRequest();
        ojRequest.setQid(1);
        ojRequest.setCode("/* example */\n" +
                "#include<stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "    printf(\"hello world\\n\");\n" +
                "}");
        System.out.println(questionService.onlineJudgement(ojRequest));
    }
}
