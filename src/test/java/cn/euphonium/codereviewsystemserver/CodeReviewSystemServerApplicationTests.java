package cn.euphonium.codereviewsystemserver;

import cn.euphonium.codereviewsystemserver.utils.CodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class CodeReviewSystemServerApplicationTests {

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
        String s = "#include<stdio.h>";
        CodeUtils.operatorProcess(s);
    }

}
