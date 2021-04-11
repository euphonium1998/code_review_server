package cn.euphonium.codereviewsystemserver;

import cn.euphonium.codereviewsystemserver.entity.Question;
import cn.euphonium.codereviewsystemserver.entity.Sample;
import cn.euphonium.codereviewsystemserver.mapper.QuestionMapper;
import cn.euphonium.codereviewsystemserver.mapper.UserMapper;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import cn.euphonium.codereviewsystemserver.utils.CodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    void insertQ() {

        Question q = new Question();
        q.setName("test test");
        q.setAccount("170110317");
        q.setQuestionDescription("alielie");
        q.setInputDescription("bilibili");
        q.setOutputDescription("acfun");
        q.setSampleInput("1 2 3\n" +
                "4 5 6 7\n" +
                "2 3");
        q.setSampleOutput("2 3 4");
        int i = questionMapper.insertOneQuestion(q);
        System.out.println(i);
    }

    @Test
    void selectAllQ() {
        List<Question> q = questionMapper.selectAllQuestion();
        System.out.println(q);
    }

    @Test
    void insertOneSample() {
        Sample s = new Sample();
        s.setPid(1);
        s.setInput("3 5");
        s.setOutput("8");
        questionMapper.insertOneSample(s);
    }

    @Test
    void selectOneQuestion() {
        Question q = questionMapper.selectOneQuestion(1);
        System.out.println(q);
    }

    @Test
    void testTransactional() {
        try {
            questionService.insertOneQuestion(new Question());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("false");
        }
    }

    @Test
    void testGetName() {
        System.out.println(userMapper.getNameByAccount("170110314"));
    }

    @Test
    void selectOneQuestionById() {
        System.out.println(questionService.selectOneQuestionById(1));
    }

}
