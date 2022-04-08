package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lucky
 * @description: 媒体账号的一些信息
 * @date 2022/3/16 14:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("media_user")
public class MediaUser {
    //主键id
    @TableId(value = "id" , type = IdType.AUTO)
    private Long id ;

    //媒体用户的uid
    @TableField(value = "creator_uid")
    private String creatorUid ;

    //媒体的名称
    @TableField(value = "author")
    private String author ;

    //媒体的粉丝数
    @TableField(value = "follower_count")
    private Long followerCount ;

    //媒体的描述
    @TableField(value = "description")
    private String description ;

    private String avatorUrl ;
}
