package com.hfut.newsbackend.pojo.show;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/22 13:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserInfo {
    //id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //账号（手机号）
    private String account;

    //密码
    private String password;

    //昵称
    private String nickname;

    //头像
    private String avator;

    //简述
    private String description;

    //文章数
    private Long articleCount;

    //获赞数
    private Long diggCount;

    //粉丝数
    private Long followerCount;

    //关注用户数
    private Long focuserCount;

    //性别
    private Integer sex;

    //用户类型
    private Integer userType;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //逻辑删除(0代表未删除，1代表已删除)
    private Integer delFlag ;
}
