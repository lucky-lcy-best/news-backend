package com.hfut.newsbackend.pojo.show;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Lucky
 * @description: TODO 这才是展示给前端的回复  类
 * @date 2022/4/1 14:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyArray {

    private Long id ;

    private String userName ;

    private String avator ;

    //后台时间格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date replyTime ;

    private String content ;

    private Long diggCount ;

    private Boolean isDigg ;

    private String pic ;

    private ReplyInfo replyTo ;
}
