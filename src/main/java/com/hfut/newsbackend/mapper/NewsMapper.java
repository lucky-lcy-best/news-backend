package com.hfut.newsbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hfut.newsbackend.pojo.base.News;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 新闻Dao层的CRUD操作
 */
@Repository
public interface NewsMapper extends BaseMapper<News> {

    //连接查询   查询新闻数据以及他的作者信息
    IPage<NewsInfo> getNewsInfo(IPage<NewsInfo> page, @Param("ew") Wrapper<NewsInfo> queryWrapper);
}
