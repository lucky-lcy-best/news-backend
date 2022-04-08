package com.hfut.newsbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hfut.newsbackend.pojo.show.NewsInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsInfoMapper extends BaseMapper<NewsInfo> {
    /**
     * desc   新闻表和媒体用户表连接查询
     *        注：连接最好用join，不要用where
     * @param page
     * @param queryWrapper
     * @param <P>
     * @return
     */
    @Select("SELECT DISTINCT news_info.*, media_user.author,media_user.avator_url\n" +
            "FROM news_info JOIN `media_user` ON news_info.creator_uid = media_user.creator_uid ${ew.customSqlSegment}")
    <P extends IPage<NewsInfo>> P getNewsInfo(P page, @Param("ew") Wrapper<NewsInfo> queryWrapper);

    @Select("SELECT DISTINCT news_info.*, media_user.author,media_user.avator_url\n" +
            "FROM news_info JOIN `media_user` ON news_info.creator_uid = media_user.creator_uid ${ew.customSqlSegment}")
    NewsInfo getById(@Param("ew") Wrapper<NewsInfo> queryWrapper) ;

}
