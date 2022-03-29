package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.NewsServiceImpl;
import com.hfut.newsbackend.utils.JSONResult;
import io.swagger.annotations.Api;
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
    private NewsServiceImpl newsService ;

    @GetMapping("/getRecNews/{id}/{refresh_count}/{pageSize}")
    @ApiOperation("返回前端不同分类情况下的新闻 ,identity就是分类")
    public JSONResult getRecNews(@PathVariable Integer id, @PathVariable Integer refresh_count,  @PathVariable Integer pageSize) {
        return JSONResult.ok(newsService.getRecNews(id , refresh_count, pageSize)) ;
    }

    /**
     * 根据id查询新闻信息 注意更新redis中的点赞数和收藏数
     */
    @GetMapping("/news/{id}")
    @ApiOperation("id查新闻")
    public ResponseResult selectNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id) ;
    }

    /**
     * TODO 用户点赞，点赞数加1；取消赞；点赞数-1，根据参数确定，1代表点赞，0代表取消点赞
     */
    @GetMapping("/news/clickDigg/{id}/{isDigg}")
    @ApiOperation("用户点击点赞按钮事件")
    public ResponseResult clickDigg(@PathVariable Long id ,@PathVariable Boolean isDigg) {
        return newsService.clickDigg(id ,isDigg);
    }

    /**
     * TODO 用户点赞，点赞数加1；取消赞；点赞数-1，根据参数确定，1代表点赞，0代表取消点赞
     */
    @GetMapping("/news/clickDigg/{userId}/{newsId}/{isDigg}")
    @ApiOperation("用户点击点赞按钮事件")
    public ResponseResult userDigged(@PathVariable Long userId ,@PathVariable Long newsId ,@PathVariable Boolean isDigg) {
        return newsService.userDigged(userId, newsId ,isDigg);
    }

    /**
     * TODO 判断当前用户是否已经点赞过了
     */
    @GetMapping("/news/isDigg/{userId}/{newsId}")
    public ResponseResult isUserDigged(@PathVariable Long userId ,@PathVariable Long newsId) {
        return newsService.isUserDigged(userId , newsId);
    }

    /**
     * TODO 用户收藏功能
     */
    @GetMapping("/news/clickLike/{userId}/{newsId}/{isLike}")
    @ApiOperation("用户点击点赞按钮事件")
    public ResponseResult userLiked(@PathVariable Long userId ,@PathVariable Long newsId ,@PathVariable Boolean isLike) {
        return newsService.userLiked(userId, newsId ,isLike);
    }

    /**
     * TODO 判断当前用户是否已经收藏过了
     */
    @GetMapping("/news/isLike/{userId}/{newsId}")
    public ResponseResult isUserLiked(@PathVariable Long userId ,@PathVariable Long newsId) {
        return newsService.isUserLiked(userId , newsId);
    }
}
