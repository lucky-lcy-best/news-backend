package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.show.ReplyDigg;
import com.hfut.newsbackend.response.ResponseResult;

public interface ReplyService {
    ResponseResult getAllReplys(Long commentId, Long pageNo, Long userId);

    ResponseResult addReply(Reply reply);

    ResponseResult clickDigg(ReplyDigg replyDigg);

    ResponseResult deleteById(Long replyId);
}
