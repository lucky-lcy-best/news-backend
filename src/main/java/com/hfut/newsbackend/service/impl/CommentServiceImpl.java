package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.CommentDiggMapper;
import com.hfut.newsbackend.mapper.CommentMapper;
import com.hfut.newsbackend.mapper.ReplyMapper;
import com.hfut.newsbackend.mapper.UserMapper;
import com.hfut.newsbackend.pojo.base.Comment;
import com.hfut.newsbackend.pojo.base.Reply;
import com.hfut.newsbackend.pojo.base.User;
import com.hfut.newsbackend.pojo.show.CommentDigg;
import com.hfut.newsbackend.pojo.show.CommentInfo;
import com.hfut.newsbackend.pojo.show.ReplyInfo;
import com.hfut.newsbackend.response.ResponseResult;
import com.hfut.newsbackend.service.inter.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Lucky
 * @description: TODO
 * @date 2022/3/30 16:49
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper ;

    @Autowired
    private UserMapper userMapper ;

    @Autowired
    private ReplyMapper replyMapper ;

    @Autowired
    private CommentDiggMapper commentDiggMapper ;

    /**
     * TODO 添加一条评论
     * @param comment
     * @return
     */
    @Override
    public ResponseResult addComment(Comment comment) {
        if (commentMapper.insert(comment) != 0) {
            return new ResponseResult(200,"评论成功",true) ;
        }
        return new ResponseResult(200,"评论失败",false) ;
    }

    /**
     * TODO 获取某条新闻下的评论和所有回复  分页返回 同时根据userId 判断是否对该评论点赞过
     * @param
     * @return
     */
    @Override
    public ResponseResult getAllComments(Long newsId, Integer page , Long userId) {
        //根据id获得所有评论先
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("news_id" , newsId) ;
        wrapper.orderByDesc("create_time") ;
//        List<Comment> comments = commentMapper.selectList(wrapper);
        Page<Comment> page1 = new Page<>(page , 10) ;
        Page<Comment> commentPage = commentMapper.selectPage(page1, wrapper);
        List<Comment> comments = commentPage.getRecords() ;
        Long num = commentPage.getPages() ;
        List<CommentInfo> commentInfoList = new ArrayList<>() ;
        //循环遍历取出所有评论的id，然后再去回复表查所有回复 同时查询用户的信息
        for (Comment comment : comments) {
            User user = userMapper.selectById(comment.getUserId());
            CommentInfo info = new CommentInfo();
            info.setId(comment.getId());
            info.setUserName(user.getNickname());
            info.setUserId(user.getId());
            info.setCommentTime(comment.getCreateTime());
            info.setContent(comment.getContent());
            info.setAvator(user.getAvator());
            info.setPic(comment.getCommentPic());
//            info.setLike(comment.getIsDigg());
            //查一下点赞数
            QueryWrapper<Comment> wrapper1 = new QueryWrapper<>() ;
            wrapper1.eq("id" , comment.getId()) ;
            info.setDiggCount(commentMapper.selectOne(wrapper1).getDiggCount());
            //查一下当前用户是否点赞过
            QueryWrapper<CommentDigg> wrapper3 = new QueryWrapper<>() ;
            wrapper3.eq("comment_id",comment.getId()) ;
            wrapper3.eq("user_id" , userId) ;
            if (!Objects.isNull(commentDiggMapper.selectOne(wrapper3))) {
                info.setIsDigg(commentDiggMapper.selectOne(wrapper3).getIsDigg()) ;
            }
            //查询回复条数   前提是回复该评论的回复，不是回复某个回复的回复
            QueryWrapper<Reply> wrapper2 = new QueryWrapper<>() ;
            wrapper2.eq("comment_id",comment.getId()) ;
            wrapper2.eq("type",0) ;
            List<Reply> replyList = replyMapper.selectList(wrapper2);
            List<ReplyInfo> temp = new ArrayList<>() ;
            //回复大于等于两个的话 只展示前两个
            if (replyList.size() > 1) {
                //首先根据from_uid查询用户信息获取用户名 直接有参构造
                temp.add(new ReplyInfo(userMapper.selectById(replyList.get(0).getFromUid()).getNickname(), replyList.get(0).getContent() ,replyList.get(0).getReplyPic())) ;
                temp.add(new ReplyInfo(userMapper.selectById(replyList.get(1).getFromUid()).getNickname(), replyList.get(1).getContent() ,replyList.get(1).getReplyPic())) ;
            }else if (replyList.size() == 1){
                //回复只有一个
                temp.add(new ReplyInfo(userMapper.selectById(replyList.get(0).getFromUid()).getNickname(), replyList.get(0).getContent() ,replyList.get(0).getReplyPic())) ;
            }
            //查询总回复条数
            info.setAllReply(replyMapper.selectCount(new QueryWrapper<Reply>().eq("comment_id",comment.getId())));
            info.setReplyList(temp);
            //这个commentinfo已经设置好了，加入列表种
            commentInfoList.add(info) ;
        }
        //返回列表
        HashMap<String , Object> map = new HashMap<>() ;
        map.put("comments" , commentInfoList) ;
        map.put("pageNum" , num) ;
        return new ResponseResult(200 , "获取评论成功" , map);
    }

    /**
     * 给评论点赞 插入一条点赞记录之后再更新点赞数
     * @param commentDigg
     * @return
     */
    @Override
    public ResponseResult clickDigg(CommentDigg commentDigg) {
        //先查找是否存在，存在的话直接修改值即可
        QueryWrapper<CommentDigg> wrapper1 = new QueryWrapper<>() ;
        wrapper1.eq("user_id" , commentDigg.getUserId()) ;
        wrapper1.eq("comment_id" , commentDigg.getCommentId()) ;
        CommentDigg one = commentDiggMapper.selectOne(wrapper1);
        if (!Objects.isNull(one)) {
            //记录数不为0，说明存在该记录，直接修改isDigg的值即可
            one.setIsDigg(commentDigg.getIsDigg());
            commentDiggMapper.updateById(one) ;
        }else {
            //插入点赞记录
            commentDiggMapper.insert(commentDigg) ;
        }
        //更新点赞数表
        QueryWrapper<Comment> wrapper = new QueryWrapper<>() ;
        wrapper.eq("id" , commentDigg.getCommentId()) ;
        Comment comment = new Comment() ;
        comment.setId(commentDigg.getCommentId());
        //更新点赞数 点赞 + 1 反之-1
        if (commentDigg.getIsDigg()) {
            commentMapper.diggCountPlus(comment , wrapper) ;
            return new ResponseResult(200,"点赞成功",true) ;
        }
        else {
            commentMapper.diggCountMinus(comment , wrapper) ;
            return new ResponseResult(200,"取消点赞成功",false) ;
        }
    }

    /**
     * 根据id获取某条评论 的信息 包括回复
     * @param id
     * @return
     */
    @Override
    public ResponseResult getById(Long id, Long userId) {
        //
        Comment comment = commentMapper.selectById(id) ;
        User user = userMapper.selectById(comment.getUserId());
        CommentInfo info = new CommentInfo();
        info.setId(comment.getId());
        info.setUserName(user.getNickname());
        info.setUserId(user.getId());
        info.setCommentTime(comment.getCreateTime());
        info.setContent(comment.getContent());
        info.setAvator(user.getAvator());
        info.setPic(comment.getCommentPic());
//            info.setLike(comment.getIsDigg());
        //查一下点赞数
        QueryWrapper<Comment> wrapper1 = new QueryWrapper<>() ;
        wrapper1.eq("id" , comment.getId()) ;
        info.setDiggCount(commentMapper.selectOne(wrapper1).getDiggCount());
        //查一下当前用户是否点赞过
        QueryWrapper<CommentDigg> wrapper3 = new QueryWrapper<>() ;
        wrapper3.eq("comment_id",comment.getId()) ;
        wrapper3.eq("user_id" , userId) ;
        if (!Objects.isNull(commentDiggMapper.selectOne(wrapper3))) {
            info.setIsDigg(commentDiggMapper.selectOne(wrapper3).getIsDigg()) ;
        }
        //查询回复条数   前提是回复该评论的回复，不是回复某个回复的回复
        QueryWrapper<Reply> wrapper2 = new QueryWrapper<>() ;
        wrapper2.eq("comment_id",comment.getId()) ;
        wrapper2.eq("type",0) ;
        wrapper2.orderByDesc("create_time") ;
        List<Reply> replyList = replyMapper.selectList(wrapper2);
        info.setAllReply((long) replyList.size());
        List<ReplyInfo> temp = new ArrayList<>() ;
        //回复大于等于两个的话 只展示前两个
        if (replyList.size() > 1) {
            //首先根据from_uid查询用户信息获取用户名 直接有参构造
            temp.add(new ReplyInfo(userMapper.selectById(replyList.get(0).getFromUid()).getNickname(), replyList.get(0).getContent(),replyList.get(0).getReplyPic()) );
            temp.add(new ReplyInfo(userMapper.selectById(replyList.get(1).getFromUid()).getNickname(), replyList.get(1).getContent(),replyList.get(1).getReplyPic()) );
        }else if (replyList.size() == 1){
            //回复只有一个
            temp.add(new ReplyInfo(userMapper.selectById(replyList.get(0).getFromUid()).getNickname(), replyList.get(0).getContent() ,replyList.get(0).getReplyPic())) ;
        }
        info.setReplyList(temp);
        //这个commentinfo已经设置好了，加入列表种

        //返回列表
        return new ResponseResult(200 , "获取评论成功" , info);
    }

    /**
     * TODO 根据评论id删除评论，但是保留点赞信息，因为这是已经点赞过的事实；但是可以删除该评论下的回复
     * @param commentId
     * @return
     */
    @Override
    public ResponseResult deleteById(Long commentId) {
        if (commentMapper.deleteById(commentId) != 0) {
            return new ResponseResult(200,"删除成功",true) ;
        }
        return new ResponseResult(200,"删除失败",false) ;
    }
}
