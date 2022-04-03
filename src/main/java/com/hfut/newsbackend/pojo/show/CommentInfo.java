package com.hfut.newsbackend.pojo.show;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/30 21:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfo {
    private Long id ;

    private String userName ;

    private Long userId ;

    //后台时间格式化
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date commentTime ;

    private String content ;

    private String avator;

    private String pic ;

    private Long allReply ;

    private Long diggCount ;

    //这个是否点赞过与新闻点赞和收藏一致
    private Boolean isDigg = false ;

    private List<ReplyInfo> replyList ;

}
