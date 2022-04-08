package com.hfut.newsbackend.pojo.show;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/5 10:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnReadDiggs {
    private Long id ;

    private String userName ;

    private String avator ;

    private CommentInfo comment;

    private ReplyArray reply ;
    //后台时间格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date diggTime ;

    private Short type ;

}
