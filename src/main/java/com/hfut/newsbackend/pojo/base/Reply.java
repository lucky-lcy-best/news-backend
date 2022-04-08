package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2022/3/30 21:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_reply")
public class Reply {

    private Long id ;

    private Long commentId ;

    private String content ;

    private Long fromUid ;

    private Long toUid ;

    private Short type ;

    private Long diggCount ;

    private String replyPic ;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;

    @TableLogic
    private Boolean delFlag = false ;

    //被回复者是否已经查看过该通知
    private Boolean isRead = false ;
}
