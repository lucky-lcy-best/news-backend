package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.show.CommentDigg;
import com.hfut.newsbackend.pojo.show.ReplyDigg;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.ReplyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lucky
 * @description: TODO 回复相关接口
 * @date 2022/4/1 15:37
 */
@RestController
public class ReplyController {

    @Autowired
    private ReplyServiceImpl replyService ;
    /**
     * 根据comment_id获取所有的回复 同时针对当前用户，判断是否点赞过
     */
    @GetMapping("/reply/getAll/{commentId}/{pageNo}/{userId}")
    public ResponseResult getAllReplys(@PathVariable Long commentId , @PathVariable Long pageNo , @PathVariable Long userId) {
        return replyService.getAllReplys(commentId , pageNo , userId) ;
    }

    /**
     * 添加一条回复
     */
    @PostMapping("/reply/addReply")
    public ResponseResult addReply(@RequestBody Reply reply) {
        return replyService.addReply(reply) ;
    }

    /**
     * TODO 给回复点赞
     */
    @PostMapping("/reply/clickDigg")
    public ResponseResult clickDigg(@RequestBody ReplyDigg replyDigg){
        return replyService.clickDigg (replyDigg) ;
    }

    /**
     * TODO 删除某条回复
     */
    @GetMapping("/reply/deleteById/{replyId}")
    public ResponseResult deleteById(@PathVariable Long replyId) {
        return replyService.deleteById(replyId) ;
    }
}
