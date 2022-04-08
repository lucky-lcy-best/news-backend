package com.hfut.newsbackend.service.inter;

import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.show.ReplyDigg;
import com.hfut.newsbackend.response.ResponseResult;

public interface ReplyService {
    ResponseResult getAllReplys(Long commentId, Long pageNo, Long userId);

    ResponseResult addReply(Reply reply);

    ResponseResult clickDigg(ReplyDigg replyDigg);

    ResponseResult deleteById(Long replyId);

    ResponseResult getNewReplies(Long userId);

    ResponseResult getNewRepliesComment(Long userId);

    ResponseResult setReplyRead(Long replyId);

    ResponseResult getDiggs(Long userId);

    ResponseResult setDiggRead(Long diggId, Short type);
}
