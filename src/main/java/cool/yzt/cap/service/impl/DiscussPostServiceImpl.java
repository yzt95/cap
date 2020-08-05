package cool.yzt.cap.service.impl;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.DiscussPost;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.CommentMapper;
import cool.yzt.cap.mapper.DiscussPostMapper;
import cool.yzt.cap.service.CommentService;
import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.service.UserService;
import cool.yzt.cap.util.PageBeanUtil;
import cool.yzt.cap.util.RedisKeyUtil;
import cool.yzt.cap.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageBean getPageBean(int start, int limit,int mode) {
        PageHelper.startPage(start,limit);
        List<DiscussPost> posts = discussPostMapper.selectAllOutsideBlacklist(mode);
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(posts);
        List<Map<String, Object>> contents = new ArrayList<>();
        if(posts!=null) {
            for(DiscussPost post : posts) {
                Map<String,Object> content = new HashMap<>();
                content.put("post",post);
                User user = userService.findById(post.getUserId());
                content.put("user",user);
                content.put("likeCount",likeService.findEntityLikeCount(1,post.getId()));
                contents.add(content);
            }
        }
        return PageBeanUtil.getPageBean(pageInfo,contents);
    }

    @Override
    public int findCountOutsideBlacklist() {
        return discussPostMapper.selectCountOutsideBlacklist();
    }

    @Override
    public DiscussPost findById(int id) {
        return discussPostMapper.selectById(id);
    }

    @Override
    public int save(DiscussPost post) throws RuntimeException {
        if(post==null) {
            throw new RuntimeException("post 不能为空");
        }

        post.setTitle(HtmlUtil.escape(post.getTitle()));
        //post.setContent(HtmlUtil.escape(post.getContent()));
        int result =  discussPostMapper.insert(post);
        return post.getId();
    }

    @Override
    public int updateCommentCount(int postId, int commentCount) {
        return discussPostMapper.updateCommentCount(postId,commentCount);
    }

    @Override
    public PageBean findByUserId(int userId, int start, int limit) {
        PageHelper.startPage(start,limit);
        List<DiscussPost> posts = discussPostMapper.selectAllByUserId(userId);
        PageInfo<DiscussPost> pageInfo = new PageInfo<>(posts);
        List<Map<String, Object>> contents = new ArrayList<>();
        if(posts!=null) {
            for (DiscussPost post : posts) {
                Map<String, Object> content = new HashMap<>();
                content.put("date",post.getCreateTime());
                content.put("title",post.getTitle());
                content.put("likeCount",likeService.findEntityLikeCount(1,post.getId()));
                content.put("postId",post.getId());
                content.put("isTop",post.getType()==1);
                content.put("isGreat",post.getStatus()==1);
                contents.add(content);
            }
        }
        return PageBeanUtil.getPageBean(pageInfo,contents);
    }


    @Override
    public int setWonderful(int postId, int status) {
        return discussPostMapper.updateStatus(postId,status);
    }

    @Override
    public int setTop(int postId, int type) {
        return discussPostMapper.updateType(postId,type);
    }

    @Override
    public int delete(int postId) {
        return discussPostMapper.updateStatus(postId,2);
    }

    @Override
    public void calculateScore(int postId) {
        DiscussPost post = discussPostMapper.selectById(postId);
        if(post==null) return ;
        double wonderfulScore = post.getStatus()==1 ? 100 : 0;
        double commentScore = 5.0*post.getCommentCount();
        double likeScore = 3.0*likeService.findEntityLikeCount(1,postId);
        double score = ((double)post.getCreateTime().getTime()/(1000.0*3600*24*36)) + Math.log10(wonderfulScore+commentScore+likeScore);
        discussPostMapper.updateScore(postId,score);
    }
}
