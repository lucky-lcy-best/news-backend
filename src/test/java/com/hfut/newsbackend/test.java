package com.hfut.newsbackend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hfut.newsbackend.mapper.UserMapper;
import com.hfut.newsbackend.pojo.base.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class test {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserMapper(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount,"18856357393");
        userMapper.delete(wrapper);
    }

    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;
        String encoder = passwordEncoder.encode("123456") ;
        System.out.println(encoder);
    }
}