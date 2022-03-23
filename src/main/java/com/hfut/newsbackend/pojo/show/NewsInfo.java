package com.hfut.newsbackend.pojo.show;

import com.baomidou.mybatisplus.annotation.TableField;
import com.hfut.newsbackend.pojo.base.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lucky
 * @description: 列表页新闻所需要展示的所有数据，包括新闻的详情何作者名称
 * @date 2022/3/16 15:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsInfo extends News {
    //继承News类的所有属性
    @TableField(value = "author")
    private String author ;

    //媒体用户的头像
    @TableField(value = "avator_url")
    private String avatorUrl ;

}
