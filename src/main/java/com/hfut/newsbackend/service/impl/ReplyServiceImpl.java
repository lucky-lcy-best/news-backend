package com.hfut.newsbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hfut.newsbackend.mapper.ReplyDiggMapper;
import com.hfut.newsbackend.mapper.ReplyMapper;
import com.hfut.newsbackend.mapper.UserMapper;
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
                Reply reply1 = replyMapper.selectById(reply.getToUid()) ;
                ReplyInfo replyInfo = new ReplyInfo(userMapper.selectById(reply1.getFromUid()).getNickname(), reply1.getContent(),reply1.getReplyPic());
                info.setReplyTo(replyInfo);
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
            return new ResponseResult(200,"删除成功",true) ;
        }
        return new ResponseResult(200,"删除失败",false) ;
    }
}
