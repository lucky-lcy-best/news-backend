package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.*;
import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.base.User;
import com.hfut.newsbackend.pojo.show.*;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.inter.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/4/1 16:06
 */
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ReplyDiggMapper replyDiggMapper ;

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private CommentDiggMapper commentDiggMapper ;

    /**
     * 获取某条评论下的所有回复
     * @param commentId
     * @param pageNo
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getAllReplys(Long commentId, Long pageNo, Long userId) {
        //根据id获得所有回复先
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("comment_id" , commentId) ;
        wrapper.orderByDesc("create_time") ;
        Page<Reply> page1 = new Page<>(pageNo , 10) ;
        Page<Reply> replyPage = replyMapper.selectPage(page1, wrapper);
        List<Reply> replies = replyPage.getRecords() ;
        Long num = replyPage.getPages() ;
        List<ReplyArray> replyArrayList = new ArrayList<>() ;
        //循环遍历取出所有回复的id，然后根据id再去查询形成一条回复传给前端
        for (Reply reply : replies) {
            User user = userMapper.selectById(reply.getFromUid());
            ReplyArray info = new ReplyArray();
            //设置id
            info.setId(reply.getId());
            //用户昵称
            info.setUserName(user.getNickname());
            //头像
            info.setAvator(user.getAvator());
            //回复时间
            info.setReplyTime(reply.getCreateTime());
            //回复内容
            info.setContent(reply.getContent());
            //回复图片
            info.setPic(reply.getReplyPic());
            //获赞数
            QueryWrapper<Reply> wrapper1 = new QueryWrapper<>() ;
            wrapper1.eq("id" , reply.getId()) ;
            info.setDiggCount(replyMapper.selectOne(wrapper1).getDiggCount());
            //当前用户是否点赞
            QueryWrapper<ReplyDigg> wrapper2 = new QueryWrapper<>() ;
            wrapper2.eq("reply_id",reply.getId()) ;
            wrapper2.eq("user_id" , userId) ;
            if (!Objects.isNull(replyDiggMapper.selectOne(wrapper2))) {
                info.setIsDigg(replyDiggMapper.selectOne(wrapper2).getIsDigg()) ;
            }else {
                info.setIsDigg(false) ;
            }
            //判断该回复是否有回复对象 那就是看他的type值
            if (reply.getType() == 1) {
//                Reply reply1 = replyMapper.selectById(reply.getToUid()) ;
                if (!Objects.isNull(replyMapper.selectById(reply.getToUid()))) {
                    Reply reply1 = replyMapper.selectById(reply.getToUid()) ;
                    ReplyInfo replyInfo = new ReplyInfo(userMapper.selectById(reply1.getFromUid()).getNickname(), reply1.getContent(),reply1.getReplyPic());
                    info.setReplyTo(replyInfo);
                }
//                if (!Objects.isNull(userMapper.selectById(reply1.getFromUid()))) {
//                    ReplyInfo replyInfo = new ReplyInfo(userMapper.selectById(reply1.getFromUid()).getNickname(), reply1.getContent(),reply1.getReplyPic());
//                    info.setReplyTo(replyInfo);
//                }

            }
            replyArrayList.add(info) ;
        }
        //返回列表
        HashMap<String , Object> map = new HashMap<>() ;
        map.put("replies" , replyArrayList) ;
        map.put("pageNum" , num) ;
        return new ResponseResult(200 , "获取回复成功" , map);
    }

    /**
     * 添加一条评论
     * @param reply
     * @return
     */
    @Override
    public ResponseResult addReply(Reply reply) {
        if (replyMapper.insert(reply) != 0) {
            return new ResponseResult(200,"回复成功",true) ;
        }
        return new ResponseResult(200,"回复失败",false) ;
    }

    /**
     * 给某条回复点赞 同时更新一下点赞数
     * @param replyDigg
     * @return
     */
    @Override
    public ResponseResult clickDigg(ReplyDigg replyDigg) {
        //先查找是否存在，存在的话直接修改值即可
        QueryWrapper<ReplyDigg> wrapper1 = new QueryWrapper<>() ;
        wrapper1.eq("user_id" , replyDigg.getUserId()) ;
        wrapper1.eq("reply_id" , replyDigg.getReplyId()) ;
        ReplyDigg one = replyDiggMapper.selectOne(wrapper1);
        if (!Objects.isNull(one)) {
            //记录数不为0，说明存在该记录，直接修改isDigg的值即可
            one.setIsDigg(replyDigg.getIsDigg());
            replyDiggMapper.updateById(one) ;
        }else {
            //插入点赞记录
            replyDiggMapper.insert(replyDigg) ;
        }
        //更新点赞数表
        QueryWrapper<Reply> wrapper = new QueryWrapper<>() ;
        wrapper.eq("id" , replyDigg.getReplyId()) ;
        Reply reply = new Reply() ;
        reply.setId(replyDigg.getReplyId());
        //更新点赞数 点赞 + 1 反之-1
        if (replyDigg.getIsDigg()) {
            replyMapper.diggCountPlus(reply , wrapper) ;
            return new ResponseResult(200,"点赞成功",true) ;
        }
        else {
            replyMapper.diggCountMinus(reply , wrapper) ;
            return new ResponseResult(200,"取消点赞成功",false) ;
        }
    }

    /**
     * TODO 根据评回复的id 删除该回复
     * @param replyId
     * @return
     */
    @Override
    public ResponseResult deleteById(Long replyId) {
        if (replyMapper.deleteById(replyId) != 0) {
            //实现级联删除
            QueryWrapper<Reply> wrapper = new QueryWrapper<>() ;
            wrapper.eq("to_uid" , replyId) ;
            replyMapper.delete(wrapper) ;
            return new ResponseResult(200,"删除成功",true) ;
        }
        return new ResponseResult(200,"删除失败",false) ;
    }

    /**
     * TODO 获取未读消息数量 点赞数、评论的回复数、回复回复数
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getNewReplies(Long userId) {
        //先查看所有评论才对 根据userId
        QueryWrapper<Comment> wrapper2 = new QueryWrapper<>() ;
        wrapper2.eq("user_id" , userId) ;
        List<Comment> comments = commentMapper.selectList(wrapper2);
        //1 然后查看这些评论有多少回复 (包括回复别人的，类似微信QQ) 自己的回复不算
        //  同时查看这些评论的点赞信息哪些是未读的
        Long num = 0L ;
        for (Comment comment : comments) {
            QueryWrapper<Reply> wrapper = new QueryWrapper<>() ;
            wrapper.eq("comment_id" , comment.getId()) ;
            wrapper.eq("is_read" , 0) ;
            List<Reply> replies = replyMapper.selectList(wrapper);
            //自己的回复不算
            for (Reply reply : replies) {
                if (reply.getFromUid() != userId) {
                    num += 1 ;
                }
            }
            //根据comment_id查询点赞信息 同时还不是自己的点赞数（肯定是已知的）
            QueryWrapper<CommentDigg> wrapper1 = new QueryWrapper<>() ;
            wrapper1.eq("comment_id" , comment.getId()) ;
            wrapper1.eq("is_read" , 0) ;
            wrapper1.ne("user_id" , userId) ;
            num = num + commentDiggMapper.selectCount(wrapper1) ;

        }
        //2.1 根据userId查找出自己有哪些回复且自己是未读的
        QueryWrapper<Reply> wrapper3 = new QueryWrapper<>() ;
        wrapper3.eq("from_uid" , userId) ;
        wrapper3.eq("is_read" , 0) ;
        List<Reply> replyList = replyMapper.selectList(wrapper3);

        for (Reply reply : replyList) {
            //查询这些回复有没有未读的点赞信息（type 为1 ）
            QueryWrapper<ReplyDigg> wrapper = new QueryWrapper<>() ;
            wrapper.eq("reply_id" , reply.getId()) ;
            wrapper.eq("is_read" , 0) ;
            wrapper.ne("user_id" , userId) ;
            num = num + replyDiggMapper.selectCount(wrapper) ;
        }
        //3 接下来在查找用户发表的回复有多少人回复 前提是不在用户发表的评论之下的回复，因为这些回复已经算在上面了(未读的消息)
        QueryWrapper<Reply> wrapper = new QueryWrapper<>() ;
        wrapper.eq("type" , 1) ;
        wrapper.eq("is_read" , 0) ;
        List<Reply> replies = replyMapper.selectList(wrapper);
        for (Reply reply : replies) {
            //根据该回复的评论id判断是否为用户发表的评论之下的回复
            if (!Objects.isNull(commentMapper.selectById(reply.getCommentId()))) {
                //不是该用户的回复, 判断to_uid是不是当前用户的回复加1，还要is_read为0 回复自己的不算
                if (commentMapper.selectById(reply.getCommentId()).getUserId() != userId && replyMapper.selectById(reply.getToUid()).getFromUid() == userId) {
                    num += 1 ;
                }

            }

        }
        return new ResponseResult(200,"获取消息通知数量成功" , num);
    }

    /**
     * 获取用户未查看的评论下的回复以及回复的回复
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getNewRepliesComment(Long userId) {
        //查找该用户发表的所有评论
        QueryWrapper<Comment> wrapper2 = new QueryWrapper<>() ;
        wrapper2.eq("user_id" , userId) ;
        List<Comment> comments = commentMapper.selectList(wrapper2);
        List<UnReadMessage> messages = new ArrayList<>() ;
        //根据评论id查询回复
        for (Comment comment : comments) {
            QueryWrapper<Reply> wrapper = new QueryWrapper<>() ;
            wrapper.eq("comment_id" , comment.getId()) ;
            wrapper.eq("is_read" , 0) ;
            wrapper.orderByDesc("create_time") ;
            List<Reply> replies = replyMapper.selectList(wrapper);
            //不是自己的回复所以就是新的未读消息
            for (Reply reply : replies) {
                if (reply.getFromUid() != userId) {
                    //查询该回复者的信息
                    User user = userMapper.selectById(reply.getFromUid());
                    UnReadMessage message = new UnReadMessage() ;
                    message.setUserName(user.getNickname());
                    message.setAvator(user.getAvator());
                    message.setReplyPic(reply.getReplyPic());
                    message.setContent(reply.getContent());
                    message.setReplyTime(reply.getCreateTime());
                    message.setId(reply.getId());
                    message.setType(reply.getType());
                    //该评论内容
                    if (reply.getType() == 0) {
                        CommentInfo info = new CommentInfo() ;
                        info.setId(comment.getId());
                        info.setContent(comment.getContent());
                        info.setPic(comment.getCommentPic());
                        message.setComment(info);
                        messages.add(message);
                    }
                    //该回复内容 虽然是回复，但是用户点开该链接还是转到该评论，所以id还是评论的id
                    else {
                        //回复内容应该为我的回复内容根据to_uid获取我的回复内容
                        ReplyArray info = new ReplyArray() ;
                        info.setId(comment.getId());
                        info.setContent(replyMapper.selectById(reply.getToUid()).getContent() );
                        info.setPic(reply.getReplyPic());
                        message.setReply(info);
                        messages.add(message);
                    }
                }
            }

        }
        return new ResponseResult(200,"获取未读消息成功" , messages);
    }

    /**
     * TODO 设置回复的消息状态为已读
     * @param replyId
     * @return
     */
    @Override
    public ResponseResult setReplyRead(Long replyId ) {
        //将该replyId的记录的已读状态设置为已读
        Reply reply = new Reply() ;
        reply.setId(replyId);
        reply.setIsRead(true);
        if (replyMapper.updateById(reply) != 0) {
            return new ResponseResult(200 , "已读成功",true) ;
        }
        return new ResponseResult(200 , "已读失败",false) ;
    }


    /**
     * TODO 获取用户未读的点赞信息
     * @param userId
     * @return
     */
    @Override
    public ResponseResult getDiggs(Long userId) {
        //1 查看用户的评论的点赞信息
        QueryWrapper<Comment> wrapper = new QueryWrapper<>() ;
        wrapper.eq("user_id" , userId) ;
        List<Comment> comments = commentMapper.selectList(wrapper);
        List<UnReadDiggs> messages = new ArrayList<>() ;
        //1.1 遍历这些评论
        for (Comment comment : comments) {
            //对每一条评论，查找评论点赞表 未读且 点赞者不是用户自己的
            QueryWrapper<CommentDigg> wrapper1 = new QueryWrapper<>() ;
            wrapper1.eq("comment_id" , comment.getId()) ;
            wrapper1.eq("is_read" , 0) ;
            wrapper1.ne("user_id" , userId) ;
            wrapper1.orderByDesc("create_time") ;
            List<CommentDigg> diggs = commentDiggMapper.selectList(wrapper1);
            //遍历这些点赞信息
            for (CommentDigg digg : diggs) {
                User user = userMapper.selectById(digg.getUserId());
                UnReadDiggs message = new UnReadDiggs() ;
                message.setUserName(user.getNickname());
                message.setAvator(user.getAvator());
                message.setDiggTime(digg.getCreateTime());
                message.setId(digg.getId());
                CommentInfo info = new CommentInfo() ;
                info.setId(comment.getId());
                info.setContent(comment.getContent());
                info.setPic(comment.getCommentPic());
                message.setComment(info);
                messages.add(message);
                message.setType((short) 0);
            }

        }
        //2 遍历回复点赞表
        QueryWrapper<Reply> wrapper1 = new QueryWrapper<>() ;
        wrapper1.eq("from_uid" , userId) ;
        List<Reply> replies = replyMapper.selectList(wrapper1);
        for (Reply reply : replies) {
            QueryWrapper<ReplyDigg> wrapper2 = new QueryWrapper<>() ;
            wrapper2.eq("reply_id" , reply.getId()) ;
            wrapper2.eq("is_read" , 0) ;
            wrapper2.ne("user_id" , userId) ;
            wrapper2.orderByDesc("create_time") ;
            List<ReplyDigg> diggs = replyDiggMapper.selectList(wrapper2);
            for (ReplyDigg digg : diggs) {
                User user = userMapper.selectById(digg.getUserId());
                UnReadDiggs message = new UnReadDiggs() ;
                message.setUserName(user.getNickname());
                message.setAvator(user.getAvator());
                message.setDiggTime(digg.getCreateTime());
                message.setId(digg.getId());
                ReplyArray info = new ReplyArray() ;
                //还是保存评论的id 根据reply_id查询
                info.setId(replyMapper.selectById(reply.getId()).getCommentId());
                info.setContent(reply.getContent());
                info.setPic(reply.getReplyPic());
                message.setReply(info);
                message.setType((short) 1);
                messages.add(message);
            }
        }
        return new ResponseResult(200,"获取未读点赞成功" , messages);
    }

    /**
     * TODO 设置点赞状态为已读
     * @param diggId
     * @return
     */
    @Override
    public ResponseResult setDiggRead(Long diggId, Short type) {
        if (type == 1) {
            ReplyDigg replyDigg = new ReplyDigg() ;
            replyDigg.setId(diggId);
            replyDigg.setIsRead(true);

            if (replyDiggMapper.updateById(replyDigg) != 0) {
                return new ResponseResult(200 , "已读成功",true) ;
            }
        }
        else {
            CommentDigg commentDigg = new CommentDigg() ;
            commentDigg.setId(diggId);
            commentDigg.setIsRead(true) ;

            if (commentDiggMapper.updateById(commentDigg) != 0) {
                return new ResponseResult(200 , "已读成功",true) ;
            }

        }

        return new ResponseResult(200 , "已读失败",false) ;
    }


}
