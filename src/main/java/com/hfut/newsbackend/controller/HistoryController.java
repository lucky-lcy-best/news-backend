package com.hfut.newsbackend.controller;

import com.hfut.newsbackend.pojo.base.UserHistory;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lucky
 * @description: TODO 浏览历史
 * @date 2022/4/3 11:27
 */
@RestController
public class HistoryController {

    @Autowired
    private NewsServiceImpl newsService ;

    /**
     * 批量插入浏览记录 还是写在newsService层
     */
    @PostMapping("/news/addHistory")
    public ResponseResult addHistory(@RequestBody UserHistory userHistory) {
        return newsService.addHistory(userHistory) ;
    }

    /**
     * 获取某一用户的历史记录
     */
    @GetMapping("/news/getAllHistory/{userId}/{pageNo}")
    public ResponseResult getAllHistory(@PathVariable Long userId , @PathVariable Long pageNo) {
        return newsService.getAllHistory(userId , pageNo) ;
    }

    /**
     * 获取某一用户的收藏
     */
    @GetMapping("/news/getAllLike/{userId}/{pageNo}")
    public ResponseResult getAllLike(@PathVariable Long userId , @PathVariable Long pageNo) {
        return newsService.getAllLike(userId , pageNo) ;
    }
}
