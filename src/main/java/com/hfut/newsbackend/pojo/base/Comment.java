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
 * @author Lucky
 * @description: TODO
 * @date 2022/3/30 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_comment")
public class Comment {

    //id
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id ;

    //news_id
    private Long newsId ;

    //content
    private String content ;

    //user_id
    private Long userId ;

    //点赞数
    private Long diggCount ;

    //图片
    private String commentPic ;

    //
    private Date createTime ;

    private Date updateTime ;

    //逻辑删除
    @TableLogic
    private Boolean delFlag = false ;
}
