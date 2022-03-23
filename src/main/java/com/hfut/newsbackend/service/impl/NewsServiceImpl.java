package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.MediaUserMapper;
import com.hfut.newsbackend.mapper.NewsInfoMapper;
import com.hfut.newsbackend.mapper.NewsMapper;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import com.hfut.newsbackend.service.inter.NewsService;
import com.hfut.newsbackend.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private MediaUserMapper mediaUserMapper ;

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
}
