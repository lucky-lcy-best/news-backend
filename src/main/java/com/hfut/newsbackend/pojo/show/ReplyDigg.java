package com.hfut.newsbackend.pojo.show;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/1 15:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("reply_digg")
public class ReplyDigg {
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id ;

    private Long userId ;

    private Long replyId ;

    private Boolean isDigg = false ;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;

    //被回复者是否已经查看过该通知
    private Boolean isRead = false ;

}
