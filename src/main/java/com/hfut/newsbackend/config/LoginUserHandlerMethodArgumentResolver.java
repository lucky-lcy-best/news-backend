//package com.hfut.newsbackend.config;
//
//import com.hfut.newsbackend.pojo.show.LoginUser;
//import org.springframework.core.MethodParameter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
///**
// * @author Lucky
// * @description: TODO
// * @date 2022/3/22 11:14
// */
//@Component
//public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.hasParameterAnnotation(CurrentUser.class) &&
//                parameter.getParameterType().isAssignableFrom(User.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
//                                  NativeWebRequest request, WebDataBinderFactory factory) {
//        // header中获取用户token
//        String token = request.getHeader("token");
//        //从redis中获取用户信息
//        String redisKey = "login:" + userid;
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);
//        // TODO 根据userId获取User信息，这里省略，直接创建一个User对象。
//        User user = new User();
//        user.setName("Tom");
//        user.setUserId(userId);
//        return user;
//    }
//}
//
