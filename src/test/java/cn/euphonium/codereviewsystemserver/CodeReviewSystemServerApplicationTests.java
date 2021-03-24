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
        String test = "#include<stdio.h>\n" +
                "  int main() { //code he\"\"re1\n" +
                "           printf(\"hello\\n\");\n" +
                "return 0;\n" +
                "    /*\n" +
                "    code he're'2\n" +
                "    */\n" +
                "  }";
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

}
