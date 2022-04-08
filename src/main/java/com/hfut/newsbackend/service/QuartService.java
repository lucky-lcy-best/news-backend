package com.hfut.newsbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfut.newsbackend.mapper.NewsMapper;
import com.hfut.newsbackend.mapper.UserDiggMapper;
import com.hfut.newsbackend.mapper.UserHistoryMapper;
import com.hfut.newsbackend.mapper.UserLikeMapper;
import com.hfut.newsbackend.pojo.base.News;
import com.hfut.newsbackend.pojo.base.UserHistory;
import com.hfut.newsbackend.pojo.show.UserDigg;
import com.hfut.newsbackend.pojo.show.UserLike;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author Lucky
 * @description: TODO  定时类
 * @date 2022/3/28 19:51
 */
@Component
public class QuartService {

    @Autowired
    private RedisCache redisCache ;

    @Autowired
    private UserDiggMapper userDiggMapper ;

    @Autowired
    private UserLikeMapper userLikeMapper ;

    @Autowired
    private NewsMapper newsMapper ;

    @Autowired
    private UserHistoryMapper userHistoryMapper ;

    //每 5 分钟写回一次点赞数和信息回数据库  防止数据库压力过大
    @Scheduled(cron = "0/5 * * * * ?")
    public void digg2DB() {
        //获取所有的键值对
        Collection<String> keys = redisCache.keys("*::*") ;
        //遍历每一个点赞信息并持久化到数据库
        for (String key : keys) {
            String[] ids = key.split("::") ;
            String userId = ids[0] ;
            String newsId = ids[1] ;
            Boolean isDigg = redisCache.getCacheObject(key) ;
            UserDigg userDigg = new UserDigg();
            userDigg.setUserId(Long.parseLong(userId));
            userDigg.setNewsId(Long.parseLong(newsId));
            userDigg.setIsDigg(isDigg);
            //插入UserDigg表   先查找是否已经存在
            QueryWrapper<UserDigg> wrapper = new QueryWrapper<>() ;
            wrapper.eq("user_id" ,userId) ;
            wrapper.eq("news_id" , newsId) ;
            UserDigg one = userDiggMapper.selectOne(wrapper) ;
            if (Objects.isNull(one)) userDiggMapper.insert(userDigg) ;
            else {
                //根据id更新一下值
                one.setIsDigg(isDigg);
                userDiggMapper.updateById(one) ;
            }
            News news = new News() ;
            news.setId(Long.parseLong(newsId)) ;
            QueryWrapper<News> updateWrapper = new QueryWrapper<>() ;
            updateWrapper.eq("id" , Long.parseLong(newsId)) ;
            //更新news_info表的点赞数 + 1 或者减一
            if (isDigg) newsMapper.diggCountPlus(news , updateWrapper);
            else newsMapper.diggCountMinus(news , updateWrapper);
            //更新完之后删除redis中的数据
            redisCache.deleteObject(key);
        }

    }

    //每 5 分钟写回一次收藏数和信息回数据库  防止数据库压力过大
    @Scheduled(cron = "0/5 * * * * ?")
    public void like2DB() {
        //获取所有的键值对
        Collection<String> keys = redisCache.keys("*==*") ;
        //遍历每一个收藏信息并持久化到数据库
        for (String key : keys) {
            String[] ids = key.split("==") ;
            String userId = ids[0] ;
            String newsId = ids[1] ;
            Boolean isLike = redisCache.getCacheObject(key) ;
            UserLike userLike = new UserLike();
            userLike.setUserId(Long.parseLong(userId));
            userLike.setNewsId(Long.parseLong(newsId));
            userLike.setIsLike(isLike);
            //插入UserDigg表   先查找是否已经存在
            QueryWrapper<UserLike> wrapper = new QueryWrapper<>() ;
            wrapper.eq("user_id" ,userId) ;
            wrapper.eq("news_id" , newsId) ;
            UserLike one = userLikeMapper.selectOne(wrapper) ;
            if (Objects.isNull(one)) userLikeMapper.insert(userLike) ;
            else {
                //根据id更新一下值
                one.setIsLike(isLike);
                userLikeMapper.updateById(one) ;
            }
            News news = new News() ;
            news.setId(Long.parseLong(newsId)) ;
            QueryWrapper<News> updateWrapper = new QueryWrapper<>() ;
            updateWrapper.eq("id" , Long.parseLong(newsId)) ;
            //更新news_info表的点赞数 + 1 或者减一
            if (isLike) newsMapper.likeCountPlus(news , updateWrapper);
            else newsMapper.likeCountMinus(news , updateWrapper);
            //更新完之后删除redis中的数据
            redisCache.deleteObject(key);
        }

    }

    //每 5 分钟写回一次历史记录和信息回数据库  防止数据库压力过大
    @Scheduled(cron = "0/5 * * * * ?")
    public void history2DB() {
        //获取所有的键值对
        Collection<String> keys = redisCache.keys("*..") ;
        //遍历每一个收藏信息并持久化到数据库
        for (String key : keys) {
            //获取userId
            String userId = key.substring(0,key.length()-2) ;
            Long newsId = redisCache.getCacheObject(key) ;
            UserHistory userHistory = new UserHistory() ;
            userHistory.setUserId(Long.parseLong(userId));
            userHistory.setNewsId(newsId);

            QueryWrapper<UserHistory> wrapper = new QueryWrapper<>() ;
            wrapper.eq("user_id" ,userId) ;
            wrapper.eq("news_id" , newsId) ;
            UserHistory one = userHistoryMapper.selectOne(wrapper) ;
            if (Objects.isNull(one)) userHistoryMapper.insert(userHistory) ;
            else {
                //更新一下浏览次数 + 1
                one.setReadCount(one.getReadCount() + 1);
                userHistoryMapper.updateById(one) ;
            }
            redisCache.deleteObject(key);
        }

    }
}
