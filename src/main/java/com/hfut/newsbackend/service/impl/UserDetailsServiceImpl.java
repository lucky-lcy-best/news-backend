package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hfut.newsbackend.mapper.UserMapper;
import com.hfut.newsbackend.pojo.base.User;
import com.hfut.newsbackend.pojo.show.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/21 10:54
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount,username);
        User user = userMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中
        List<String> authorities = new ArrayList<>() ;
        //一共三种用户，分别赋予不同的权限
        switch (user.getUserType()) {
            case 2 : authorities = Arrays.asList("admin" , "common") ; break;
            case 1 : authorities = Arrays.asList("news", "common") ; break;
            case 0 : authorities = Arrays.asList("common") ; break;
        }
        //封装成UserDetails对象返回
        return new LoginUser(user, authorities);
    }
}