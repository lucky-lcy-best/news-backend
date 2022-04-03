package com.hfut.newsbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.base.News;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    @Update("UPDATE user_comment SET digg_count = digg_count + 1 ${ew.customSqlSegment}")
    int diggCountPlus(@Param("et") Comment entity, @Param("ew") Wrapper<Comment> updateWrapper);

    @Update("UPDATE user_comment SET digg_count = digg_count - 1 ${ew.customSqlSegment}")
    int diggCountMinus(@Param("et") Comment entity, @Param("ew") Wrapper<Comment> updateWrapper);
}
