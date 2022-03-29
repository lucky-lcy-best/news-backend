package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.*;
import com.hfut.newsbackend.pojo.base.News;
import com.hfut.newsbackend.pojo.show.LoginUser;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import com.hfut.newsbackend.pojo.show.UserDigg;
import com.hfut.newsbackend.pojo.show.UserLike;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.inter.NewsService;
import com.hfut.newsbackend.utils.DateFormatUtil;
import com.hfut.newsbackend.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/16 21:41
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsMapper newsMapper ;

    @Autowired
    private NewsInfoMapper newsInfoMapper ;

    @Autowired
    private UserDiggMapper userDiggMapper ;

    @Autowired
    private UserLikeMapper userLikeMapper ;

    @Autowired
    private MediaUserMapper mediaUserMapper ;

    @Autowired
    private RedisCache redisCache ;

    //分页查询  一次pageSize条新闻
    public List<NewsInfo> getRecNews(Integer id, Integer refresh_count, Integer pageSize) {
        String identity = new String() ;
        switch (id) {
            case 0 : identity = "关注"; break;
            case 1 : identity = "推荐"; break;
            case 2 : identity = "热点"; break;
            case 3 : identity = "数码"; break ;
            case 4 : identity = "科技"; break ;
            case 5 : identity = "军事"; break ;
            case 6 : identity = "财经"; break ;
            case 7 : identity = "体育"; break ;
            case 8 : identity = "国际"; break ;
            case 9 : identity = "娱乐"; break ;
            case 10 : identity = "美食"; break ;
        }

        Page<NewsInfo> page = new Page<>(refresh_count,pageSize);
        QueryWrapper<NewsInfo> wrapper = new QueryWrapper<>() ;
        wrapper.eq("identity", identity) ;
        newsInfoMapper.getNewsInfo(page , wrapper) ;

        //将新闻的时间由时间戳转换成datatime
        for (NewsInfo article : page.getRecords()) {
            article.setPublishTime(DateFormatUtil.timeStamp2Date(article.getPublishTime()));
            //将视频加载中这类新闻的视频加载时删除
            article.setContent(article.getContent().replace("视频加载中...",""));
        }
        return page.getRecords() ;
    }

    /**
     * 更新点赞数
     * @param id
     * @param isDigg
     * @return
     */
    @Override
    public ResponseResult clickDigg(Long id ,Boolean isDigg) {
        News news = new News() ;
        news.setId(id) ;
        QueryWrapper<News> updateWrapper = new QueryWrapper<>() ;
        updateWrapper.eq("id" , id) ;
        //点赞数+1
        if (isDigg && newsMapper.diggCountPlus(news , updateWrapper) != 0) {
            return new ResponseResult(200 , "点赞数+1成功",true) ;
        }
        else if (!isDigg && newsMapper.diggCountMinus(news , updateWrapper) != 0) {
            return new ResponseResult(200 , "点赞数-1成功",false) ;
        }
        return new ResponseResult(201 , "点赞数更新失败",false);
    }

    /**
     * id查新闻
     * @param id
     * @return
     */
    @Override
    public ResponseResult getNewsById(Long id) {
        QueryWrapper<NewsInfo> wrapper = new QueryWrapper<>() ;
        wrapper.eq("news_info.id" , id) ;
        NewsInfo one = newsInfoMapper.getById(wrapper) ;
        //得到redis中的点赞数
//        Collection<String> keys = redisCache.keys("*::*") ;
//        for (String key : keys) {
//            if (key.split("::")[1].equals(Long.toString(id))) {
//                if (redisCache.getCacheObject(key)) one.setDiggCount(one.getDiggCount() + 1);
//                else one.setDiggCount(one.getDiggCount() - 1) ;
//            }
//        }
//        //得到redis中的收藏数
//        Collection<String> keys2 = redisCache.keys("*==*") ;
//        for (String key : keys) {
//            if (key.split("==")[1].equals(Long.toString(id))) {
//                if (redisCache.getCacheObject(key)) one.setFollowerCount(one.getFollowerCount() + 1);
//                else one.setFollowerCount(one.getFollowerCount() - 1);
//            }
//        }
        //TODO 更新评论数
        return new ResponseResult(200 , "查找成功" , one);
    }

    /**
     * 用户点赞与取消点赞  redis
     * @param userId
     * @param newsId
     * @param isDigg
     * @return
     */
    @Override
    public ResponseResult userDigged(Long userId, Long newsId, Boolean isDigg) {
        // 先将信息存入redis
        String key = Long.toString(userId) + "::" + Long.toString(newsId) ;
        redisCache.setCacheObject(key , isDigg);
        return isDigg ? new ResponseResult(200, "点赞成功",true) : new ResponseResult(200, "取消点赞成功",false);
    }

    /**
     * 判断是否点赞过
     * @param userId
     * @param newsId
     * @return
     */
    @Override
    public ResponseResult isUserDigged(Long userId, Long newsId) {
        //先在redis中判断是否已经点赞过
        Collection<String> keys = redisCache.keys("*::*") ;
        for (String key : keys) {
            //如果存在并且判断状态
            if (Long.toString(userId).equals(key.split("::")[0]) && Long.toString(newsId).equals(key.split("::")[1])) {
                if (redisCache.getCacheObject(key)) return new ResponseResult(200 , "已点赞",true) ;
                else return new ResponseResult(200 , "未点赞",false) ;
            }
            //否则再去数据库查找看一看
            else {
                QueryWrapper<UserDigg> wrapper = new QueryWrapper<>() ;
                wrapper.eq("user_id" ,userId) ;
                wrapper.eq("news_id" , newsId) ;
                UserDigg userDigg = userDiggMapper.selectOne(wrapper) ;
                //查不到该记录或者isDigg为false则没有点赞
                if (Objects.isNull(userDigg) || !userDigg.getIsDigg()) {
                    return new ResponseResult(200 , "未点赞",false) ;
                }
            }
        }
        //没有进for循环说明redis一开始就清空了，直接去查数据库
        QueryWrapper<UserDigg> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" ,userId) ;
        wrapper.eq("news_id" , newsId) ;
        UserDigg userDigg = userDiggMapper.selectOne(wrapper) ;
        //查不到该记录或者isDigg为false则没有点赞
        if (Objects.isNull(userDigg) || !userDigg.getIsDigg()) {
            return new ResponseResult(200 , "未点赞",false) ;
        }
        //查到说明点赞了
        return new ResponseResult(200 , "已点赞",true) ;
    }

    /**
     * 判断是否收藏过
     * @param userId
     * @param newsId
     * @return
     */
    @Override
    public ResponseResult isUserLiked(Long userId, Long newsId) {
        //先在redis中判断是否已经点赞过
        Collection<String> keys = redisCache.keys("*==*") ;
        for (String key : keys) {
            //如果存在则返回true
            if (Long.toString(userId).equals(key.split("==")[0]) && Long.toString(newsId).equals(key.split("==")[1])) {
                if (redisCache.getCacheObject(key)) return new ResponseResult(200 , "已收藏",true) ;
                else return new ResponseResult(200 , "未收藏",false) ;
            }
            //否则再去数据库查找看一看
            else {
                QueryWrapper<UserLike> wrapper = new QueryWrapper<>() ;
                wrapper.eq("user_id" ,userId) ;
                wrapper.eq("news_id" , newsId) ;
                UserLike userLike = userLikeMapper.selectOne(wrapper) ;
                //查不到该记录或者isDigg为false则没有点赞
                if (Objects.isNull(userLike) || !userLike.getIsLike()) {
                    return new ResponseResult(200 , "未收藏",false) ;
                }
            }
        }
        //没有进for循环说明redis一开始就清空了，直接去查数据库
        QueryWrapper<UserLike> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" ,userId) ;
        wrapper.eq("news_id" , newsId) ;
        UserLike userLike = userLikeMapper.selectOne(wrapper) ;
        //查不到该记录或者isDigg为false则没有点赞
        if (Objects.isNull(userLike) || !userLike.getIsLike()) {
            return new ResponseResult(200 , "未收藏",false) ;
        }
        //查到说明点赞了
        return new ResponseResult(200 , "已收藏",true) ;
    }

    /**
     * 用户收藏与取消收藏
     * @param userId
     * @param newsId
     * @param isLike
     * @return
     */
    @Override
    public ResponseResult userLiked(Long userId, Long newsId, Boolean isLike) {
        // 先将信息存入redis
        String key = Long.toString(userId) + "==" + Long.toString(newsId) ;
        redisCache.setCacheObject(key , isLike);
        return isLike ? new ResponseResult(200, "收藏成功",true) : new ResponseResult(200, "取消收藏成功",false);
    }
}
