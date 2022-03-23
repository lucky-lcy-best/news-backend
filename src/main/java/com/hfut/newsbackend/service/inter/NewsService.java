package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.show.NewsInfo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface NewsService {

    //根据不同类别查询新闻
    public List<NewsInfo> getRecNews(Integer id , Integer refresh_count, Integer pageSize) ;
}
