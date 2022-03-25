package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.base.User;
import com.hfut.newsbackend.pojo.show.UserInfo;
import com.hfut.newsbackend.response.ResponseResult;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult getUserInfo() ;

    ResponseResult register(User user);

    ResponseResult isRegister(String account);

    ResponseResult updateUserInfo(User user);

    ResponseResult refreshToken(String id);
}
