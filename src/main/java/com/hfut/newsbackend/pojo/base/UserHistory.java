package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/3 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_read_history")
public class UserHistory {

    private Long id ;

    private Long userId ;

    private Long newsId ;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;

    //第一次插入浏览次数就是1
    private Integer readCount = 1 ;

}
