package com.hfut.newsbackend;

import com.hfut.newsbackend.mapper.NewsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsInfoMapperBackendApplicationTests {

    @Autowired
    private NewsMapper newsMapper ;
    @Test
    void contextLoads() {
        System.out.println(newsMapper.selectById(100) );

    }

}
