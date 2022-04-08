package com.hfut.newsbackend.pojo.base;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/5 15:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_follow")
public class UserFollow {

    private Long id ;

    private Long userId ;

    private String mediaUid ;

    @TableLogic
    private Boolean delFlag = false;

}
