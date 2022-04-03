package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.show.CommentDigg;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/30 16:41
 */
@RestController
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService ;

    /**
     * TODO 添加评论
     */
    @PostMapping("/comment/addComment")
    public ResponseResult addComment (@RequestBody Comment comment) {
        return commentService.addComment(comment) ;
    }

    /**
     * TODO 获取所有评论和回复
     */
    @GetMapping("/comment/getAll/{newsId}/{page}/{userId}")
    public ResponseResult getAllComments(@PathVariable Long newsId , @PathVariable Integer page, @PathVariable Long userId)  {
        return commentService.getAllComments(newsId , page , userId) ;
    }

    /**
     * 根据id获得某条评论
     */
    @GetMapping("/comment/getById/{id}/{userId}")
    public ResponseResult getById(@PathVariable Long id, @PathVariable Long userId) {
        return commentService.getById(id,userId) ;
    }

    /**
     * TODO 给评论点赞
     */
    @PostMapping("/comment/clickDigg")
    public ResponseResult clickDigg(@RequestBody CommentDigg commentDigg){
        return commentService.clickDigg (commentDigg) ;
    }

    /**
     * TODO 删除某条评论
     */
    @GetMapping("/comment/deleteById/{commentId}")
    public ResponseResult deleteById(@PathVariable Long commentId) {
        return commentService.deleteById(commentId) ;
    }
}
