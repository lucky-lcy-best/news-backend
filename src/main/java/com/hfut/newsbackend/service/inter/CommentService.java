package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.show.CommentDigg;
import com.hfut.newsbackend.response.ResponseResult;

public interface CommentService {
    ResponseResult addComment(Comment comment);

    ResponseResult getAllComments(Long newsId, Integer page,Long userId);

    ResponseResult clickDigg(CommentDigg commentDigg);

    ResponseResult getById(Long id,Long userId);

    ResponseResult deleteById(Long commentId);
}
