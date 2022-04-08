package com.hfut.newsbackend.pojo.show;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/28 16:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_digg")
public class UserDigg {

    //id
    @TableId(type = IdType.AUTO)
    private Long id ;

    //user_id
    private Long userId ;

    //news_id
    private Long newsId ;

    //is_digg 默认未点赞
    private Boolean isDigg = false;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;
}
