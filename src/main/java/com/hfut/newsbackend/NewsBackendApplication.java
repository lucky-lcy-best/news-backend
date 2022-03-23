package com.hfut.newsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hfut.newsbackend.mapper")
@SpringBootApplication
public class NewsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsBackendApplication.class, args);
    }

}
