package com.hfut.newsbackend.controller.news;

import com.hfut.newsbackend.service.impl.NewsServiceImpl;
import com.hfut.newsbackend.utils.JSONResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 针对新闻的一些接口，比如获取推荐新闻什么的
 */
@RestController
@CrossOrigin
public class NewsController {

    @Autowired
    private NewsServiceImpl newsServiceImpl ;

    @GetMapping("/getRecNews/{id}/{refresh_count}/{pageSize}")
    @ApiOperation("返回前端不同分类情况下的新闻 ,identity就是分类")
    public JSONResult getRecNews(@PathVariable Integer id, @PathVariable Integer refresh_count,  @PathVariable Integer pageSize) {
        return JSONResult.ok(newsServiceImpl.getRecNews(id , refresh_count, pageSize)) ;
    }
}
