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
 * @date 2022/3/31 15:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment_digg")
public class CommentDigg {

    @TableId(value = "id" , type = IdType.AUTO)
    private Long id ;

    private Long userId ;

    private Long commentId ;

    private Boolean isDigg = false ;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;
}
