package com.hfut.newsbackend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.base.Reply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/31 10:16
 */
@Repository
public interface ReplyMapper extends BaseMapper<Reply> {

    @Update("UPDATE user_reply SET digg_count = digg_count + 1 ${ew.customSqlSegment}")
    int diggCountPlus(@Param("et") Reply entity, @Param("ew") Wrapper<Reply> updateWrapper);

    @Update("UPDATE user_reply SET digg_count = digg_count - 1 ${ew.customSqlSegment}")
    int diggCountMinus(@Param("et") Reply entity, @Param("ew") Wrapper<Reply> updateWrapper);
}
