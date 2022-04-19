package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO 搜索历史
 * @date 2022/4/15 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("search_history")
public class SearchHistory {

    private Long id ;

    private Long userId ;

    private String keyWord ;

    @TableLogic
    private Boolean delFlag = false ;

    private Date createTime ;

    @TableField(update = "now()")
    private Date updateTime ;
}
