package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.pojo.base.User;
import com.hfut.newsbackend.pojo.show.UserInfo;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.LoginServiceImpl;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * TODO 一些用户接口 比如登录 退出等
 */
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private LoginServiceImpl loginServiceImpl ;

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('common')")  // test权限
    public String hello() {
        return "hello" ;
    }

    /**
     * 登录接口，返回token
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    @ApiOperation("用户登录接口")
    public ResponseResult login(@RequestBody User user){
        return loginServiceImpl.login(user);
    }

    /**
     * TODO app用来无缝衔接刷新token的接口
     */
        @GetMapping("/user/refreshToken/{id}")
    @ApiOperation("刷新token")
    public ResponseResult refreshToken(@PathVariable String id) {
        return loginServiceImpl.refreshToken(id) ;
    }

    /**
     * TODO 清除redis中的用户信息并退出登录
     * @return
     */
    @GetMapping("/user/logout")
    @ApiOperation("用户退出登录接口")
    public ResponseResult logout() {
        return loginServiceImpl.logout() ;
    }

    /**
     * TODO 根据token获取用户信息
     */
    @GetMapping("/user/getUserInfo")
    @ApiOperation("根据前端携带的token获取用户信息")
    public ResponseResult getUserInfo() {
        return loginServiceImpl.getUserInfo() ;
    }

    /**
     * TODO 用户注册
     */
    @PostMapping("/user/register")
    @ApiOperation("根据前端用户传来的信息注册用户")
    public ResponseResult register(@RequestBody User user) {
        return loginServiceImpl.register(user) ;
    }

    /**
     * TODO 判断该账号是否已经注册过
     */
    @GetMapping("/user/isRegister/{account}")
    @ApiOperation("是否账号已注册")
    public ResponseResult isRegister(@PathVariable String account) {
        return loginServiceImpl.isRegister(account) ;
    }

    /**
     * TODO 用户编辑资料更新信息接口
     */
    @PostMapping("/user/update")
    @ApiOperation("更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return loginServiceImpl.updateUserInfo(user) ;
    }
}
