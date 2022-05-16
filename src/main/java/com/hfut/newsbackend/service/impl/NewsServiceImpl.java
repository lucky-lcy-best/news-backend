package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.*;
import com.hfut.newsbackend.pojo.base.*;
import com.hfut.newsbackend.pojo.show.LoginUser;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import com.hfut.newsbackend.pojo.show.UserDigg;
import com.hfut.newsbackend.pojo.show.UserLike;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.inter.NewsService;
import com.hfut.newsbackend.utils.DateFormatUtil;
import com.hfut.newsbackend.utils.RedisCache;
import io.swagger.models.auth.In;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/16 21:41
 */
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private SearchHistoryMapper searchHistoryMapper ;

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

    @Autowired
    private UserHistoryMapper userHistoryMapper ;

    @Autowired
    private UserFollowMapper userFollowMapper ;

    /**
     * TODO 返回多条新闻  推荐新闻 根据用户的浏览历史记录进行推荐
     * @param id
     * @param refresh_count
     * @param pageSize
     * @return
     */
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
     * TODO 根据关键字 从新闻标题中查询相关新闻 不分页
     * @param keyword
     * @return
     */
    @Override
    public ResponseResult getNewsByTitle(String keyword) {
        //模糊查询
        QueryWrapper<NewsInfo> wrapper = new QueryWrapper<>() ;

        wrapper.like("title" , keyword) ;

        return new ResponseResult(200 , "获取新闻成功" , newsInfoMapper.getByKeyWord(wrapper));

    }

    /**
     * TODO 获取用户关注列表
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getUserFollowAuthor(Long userId) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        List<UserFollow> userFollows = userFollowMapper.selectList(wrapper) ;
        List<MediaUser> users = new ArrayList<>() ;
        for (UserFollow each : userFollows) {
            //获取media_uid
            String media_uid = each.getMediaUid();
            //查询media_user表
            QueryWrapper<MediaUser> wrapper1 = new QueryWrapper<>() ;
            wrapper1.eq("creator_uid" , media_uid) ;
            List<MediaUser> user = mediaUserMapper.selectList(wrapper1) ;
            users.add(user.get(0)) ;
        }
        return new ResponseResult(200 , "获取关注列表成功" , users);
    }

    /**
     * TODO 插入搜索历史
     * @param userId
     * @param keyWord
     * @return
     */
    @Override
    public ResponseResult addSearchHistory(Long userId, String keyWord) {
        SearchHistory searchHistory = new SearchHistory() ;
        searchHistory.setUserId(userId);
        searchHistory.setKeyWord(keyWord);
        if (searchHistoryMapper.insert(searchHistory) != 0) {
            return new ResponseResult(200 , "插入成功" , true);
        }
        return new ResponseResult(200 , "插入失败" , false);
    }

    /**
     * TODO 获取用户的历史记录
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getSearchHistory(Long userId) {
        QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        List<SearchHistory> histories = searchHistoryMapper.selectList(wrapper) ;
        List<String> keyList = new ArrayList<>();
        for (SearchHistory one : histories) {
            keyList.add(one.getKeyWord()) ;
        }
        return new ResponseResult(200 , "获取成功" , keyList);
    }

    /**
     * TODO 清空历史记录
     * @param userId
     * @return
     */
    @Override
    public ResponseResult deleteSearchHistory(Long userId) {
        QueryWrapper<SearchHistory> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        if (searchHistoryMapper.delete(wrapper) != 0) {
            return new ResponseResult(200 , "清除成功" , true);
        }
        return new ResponseResult(200 , "清除失败" , false);
    }

    /**
     * 根据用户浏览记录返回推荐的新闻
     * @param refresh_count
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult getRecNewsByHistory(Long userId ,Integer refresh_count, Integer pageSize) {
        //现根据userId查询出用户的历史记录
        QueryWrapper<UserHistory> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        List<UserHistory> histories = userHistoryMapper.selectList(wrapper);
        //遍历历史记录找到最高的几个种类 并统计个数
        HashMap<String , Integer> map = new HashMap<>() ;
        for (UserHistory history : histories) {
            Long newsId = history.getNewsId() ;
            String identity = newsMapper.selectById(newsId).getIdentity() ;
            if (map.containsKey(identity)) {
                map.put(identity , map.get(identity) + 1) ;
            }else {
                map.put(identity , 1) ;
            }
        }
        //排序
        List<Map.Entry<String , Integer>> list = new ArrayList<>(map.entrySet());

        //重写方法 降序排列
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //按照value值升序
                return o2.getValue() - o1.getValue();
                //按照value值降序
            }
        });
        //根据排序好的新闻根据种类进行推荐
        //开始查询新闻 根据条数查询 每次分页
        Integer total = histories.size() ;
        List<NewsInfo> newsInfos = new ArrayList<>() ;
        for (Map.Entry<String , Integer> c:list) {
            String identity = c.getKey() ;
            //根据比例查找新闻  时间降序
            float count = (float)c.getValue() / (float)total * (float)pageSize;
            Page<NewsInfo> page = new Page<>(refresh_count,(int)count);
            QueryWrapper<NewsInfo> wrapper2 = new QueryWrapper<>() ;
            wrapper2.eq("identity", identity) ;
            wrapper2.orderByDesc("publish_time") ;
            newsInfoMapper.getNewsInfo(page , wrapper2) ;
            //将新闻的时间由时间戳转换成 datatime
            for (NewsInfo article : page.getRecords()) {
                article.setPublishTime(DateFormatUtil.timeStamp2Date(article.getPublishTime()));
                //将视频加载中这类新闻的视频加载时删除
                article.setContent(article.getContent().replace("视频加载中...",""));
                //加入列表
                newsInfos.add(article) ;
            }
        }
        //返回结果
        return new ResponseResult(200, "推荐成功" , newsInfos) ;

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
        //将新闻的时间由时间戳转换成datatime
        one.setPublishTime(DateFormatUtil.timeStamp2Date(one.getPublishTime()));
        //将视频加载中这类新闻的视频加载时删除
        one.setContent(one.getContent().replace("视频加载中...",""));
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
     * TODO 插入浏览历史记录
     * 和点赞收藏一样，先加入redis中，每隔半个小时再写入数据库中，因为对于新闻的操作，、
     * 一般都会给数据库造成较大压力
     * @param userHistory
     * @return
     */
    @Override
    public ResponseResult addHistory(UserHistory userHistory) {
        //和点赞收藏一样，先加入redis中，每隔半个小时再写入数据库中
        //格式 userId..  :  newsId即可
        String key = Long.toString(userHistory.getUserId()) + ".." ;
        redisCache.setCacheObject(key, userHistory.getNewsId());

        return new ResponseResult(200 , "插入历史记录成功" , true) ;
    }

    /**
     * TODO 获取用户历史记录分页
     * @param userId
     * @param pageNo
     * @return
     */
    @Override
    public ResponseResult getAllHistory(Long userId, Long pageNo) {
        //先查历史记录表
        QueryWrapper<UserHistory> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        wrapper.orderByDesc("update_time") ;

        Page<UserHistory> page = new Page<>(pageNo , 10) ;
        Page<UserHistory> hisPage = userHistoryMapper.selectPage(page, wrapper);
        List<UserHistory> histories = hisPage.getRecords() ;
        Long num = hisPage.getPages() ;
        //遍历每一条新闻，根据id查询
        List<NewsInfo> newsInfos = new ArrayList<>() ;
        for (UserHistory his : histories) {
            newsInfos.add((NewsInfo) getNewsById(his.getNewsId()).getData());
        }
        HashMap<String , Object> map = new HashMap<>() ;
        map.put("histories" , newsInfos) ;
        map.put("pageNum" , num) ;
        return new ResponseResult(200 , "获取浏览记录成功" , map);
    }

    /**
     * TODO 获取用户收藏
     * @param userId
     * @param pageNo
     * @return
     */
    @Override
    public ResponseResult getAllLike(Long userId, Long pageNo) {
        //先查用户收藏表
        QueryWrapper<UserLike> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        wrapper.orderByDesc("update_time") ;

        Page<UserLike> page = new Page<>(pageNo , 10) ;
        Page<UserLike> likePage = userLikeMapper.selectPage(page, wrapper);
        List<UserLike> likes = likePage.getRecords() ;
        Long num = likePage.getPages() ;
        //遍历每一条新闻，根据id查询
        List<NewsInfo> newsInfos = new ArrayList<>() ;
        for (UserLike like : likes) {
            newsInfos.add((NewsInfo) getNewsById(like.getNewsId()).getData());
        }
        HashMap<String , Object> map = new HashMap<>() ;
        map.put("likes" , newsInfos) ;
        map.put("pageNum" , num) ;
        return new ResponseResult(200 , "获取浏览记录成功" , map);
    }

    /**
     * 获取作者信息
     * @param uid
     * @return
     */
    @Override
    public ResponseResult getAuthById(String uid) {
        QueryWrapper<MediaUser> wrapper = new QueryWrapper<>() ;
        wrapper.eq("creator_uid" , uid) ;
        List<MediaUser> users = mediaUserMapper.selectList(wrapper);
        return new ResponseResult(200,"获取作者信息成功",users.get(0));
    }

    /**
     * 获取作者发布的新闻
     * @param uid
     * @return
     */
    @Override
    public ResponseResult getNewsByAuthUid(String uid) {
        QueryWrapper<News> wrapper = new QueryWrapper<>() ;
        wrapper.eq("creator_uid" , uid) ;
        wrapper.orderByDesc("publish_time");
        List<News> news = newsMapper.selectList(wrapper);
        //将新闻的时间由时间戳转换成datatime
        for (News article : news) {
            article.setPublishTime(DateFormatUtil.timeStamp2Date(article.getPublishTime()));
            //将视频加载中这类新闻的视频加载时删除
            article.setContent(article.getContent().replace("视频加载中...",""));
        }
        return new ResponseResult(200 , "获取当前作者新闻成功", news );
    }

    /**
     * 是否专注该作者
     * @param userId
     * @param mediaUid
     * @return
     */
    @Override
    public ResponseResult userIsFollow(Long userId, String mediaUid) {
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        wrapper.eq("media_uid" , mediaUid) ;

        if (!Objects.isNull(userFollowMapper.selectOne(wrapper))) {
            return new ResponseResult(200 , "已关注" , true) ;
        }
        return new ResponseResult(200 , "未关注" , false) ;
    }

    /**
     * TODO 关注用户
     * @param userId
     * @param mediaUid
     * @return
     */
    @Override
    public ResponseResult followMedia(Long userId, String mediaUid) {
        //关注用户就是插入一条数据
        UserFollow userFollow = new UserFollow() ;
        userFollow.setUserId(userId);
        userFollow.setMediaUid(mediaUid);
        if (userFollowMapper.insert(userFollow) != 0) {
            return new ResponseResult(200 , "关注成功" , true) ;
        }
        return new ResponseResult(200 , "关注失败" , false) ;
    }

    /**
     * 取关用户
     * @param userId
     * @param mediaUid
     * @return
     */
    @Override
    public ResponseResult cancelFollowMedia(Long userId, String mediaUid) {
        //取消关注就是删除数据
        QueryWrapper<UserFollow> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        wrapper.eq("media_uid" , mediaUid) ;

        if (userFollowMapper.delete(wrapper) != 0) {
            return new ResponseResult(200 , "取关成功" , true) ;
        }
        return new ResponseResult(200 , "取关失败" , false) ;
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
