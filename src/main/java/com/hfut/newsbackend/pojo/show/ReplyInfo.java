package com.hfut.newsbackend.pojo.show;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/30 21:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyInfo {
    private String userName ;

    private String replyContent ;

    private String pic ;
}
