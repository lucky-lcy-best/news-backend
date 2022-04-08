package com.hfut.newsbackend.mapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.base.UserHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryMapper extends BaseMapper<UserHistory> {
    @Update("UPDATE user_read_history SET read_count = read_count + 1 ${ew.customSqlSegment}")
    int diggCountPlus(@Param("et") UserHistory entity, @Param("ew") Wrapper<UserHistory> updateWrapper);
}
