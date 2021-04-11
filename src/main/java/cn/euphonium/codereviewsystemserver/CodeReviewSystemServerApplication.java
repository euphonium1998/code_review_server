package cn.euphonium.codereviewsystemserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("cn.euphonium.codereviewsystemserver.mapper")
public class CodeReviewSystemServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeReviewSystemServerApplication.class, args);
    }

}
