package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.show.NewsInfo;
import com.hfut.newsbackend.response.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NewsService {

    //根据不同类别查询新闻
    List<NewsInfo> getRecNews(Integer id , Integer refresh_count, Integer pageSize) ;

    ResponseResult clickDigg(Long id ,Boolean isDigg);

    ResponseResult getNewsById(Long id);

    ResponseResult userDigged(Long userId, Long newsId, Boolean isDigg);

    ResponseResult isUserDigged(Long userId, Long newsId);

    ResponseResult userLiked(Long userId, Long newsId, Boolean isLike);

    ResponseResult isUserLiked(Long userId, Long newsId);
}
