package com.hfut.newsbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hfut.newsbackend.pojo.base.News;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 新闻Dao层的CRUD操作
 */
@Repository
public interface NewsMapper extends BaseMapper<News> {

    @Update("UPDATE news_info SET digg_count = digg_count + 1 ${ew.customSqlSegment}")
    int diggCountPlus(@Param("et") News entity, @Param("ew") Wrapper<News> updateWrapper);

    @Update("UPDATE news_info SET digg_count = digg_count - 1 ${ew.customSqlSegment}")
    int diggCountMinus(@Param("et") News entity, @Param("ew") Wrapper<News> updateWrapper);

    @Update("UPDATE news_info SET follower_count = follower_count + 1 ${ew.customSqlSegment}")
    int likeCountPlus(@Param("et") News entity, @Param("ew") Wrapper<News> updateWrapper);

    @Update("UPDATE news_info SET follower_count = follower_count - 1 ${ew.customSqlSegment}")
    int likeCountMinus(@Param("et") News entity, @Param("ew") Wrapper<News> updateWrapper);
}
