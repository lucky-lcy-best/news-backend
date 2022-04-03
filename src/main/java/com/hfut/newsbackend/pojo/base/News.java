package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 一些实体类
 * 注意驼峰命名法
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("news_info")
public class News {
    // 新闻id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //新闻标题
    private String title ;

    //新闻评论数
    private Long commentCount ;

    //新闻点赞数
    private Long diggCount ;

    //收藏数
    private Long followerCount ;

    //发布时间
    private String publishTime ;

    //作者uid
    private String creatorUid ;

    //封面url
    private String posterUrl ;

    //富文本内容
    private String content ;

    //类别
    private String identity ;

    //投诉次数
    private Long complaint ;

    //原文章url
    private String srcUrl ;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date updateTime;

    //逻辑删除
    @TableLogic
    private Boolean delFlag = false;
}
