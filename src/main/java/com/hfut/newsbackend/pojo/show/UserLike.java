package com.hfut.newsbackend.pojo.show;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/29 9:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_like")
public class UserLike {

    private Long id ;

    //user_id
    private Long userId ;

    //news_id
    private Long newsId ;

    //is_digg 默认未点赞
    private Boolean isLike = false;

    private Date createTime ;

    private Date updateTime ;

}
