package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucky
 * @description: 用户信息实体类
 * @date 2022/3/20 21:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -40356785423868312L;

    //id
    @TableId(type = IdType.AUTO)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date updateTime;

    //逻辑删除(0代表未删除，1代表已删除)
    @TableLogic
    private Boolean delFlag = false;

    //地区
    private String area ;

    //生日
    private Date birthday ;

}