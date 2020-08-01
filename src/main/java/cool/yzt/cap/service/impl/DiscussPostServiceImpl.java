package cool.yzt.cap.service.impl;
import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cool.yzt.cap.dto.PageBean;
import cool.yzt.cap.entity.DiscussPost;
import cool.yzt.cap.entity.User;
import cool.yzt.cap.mapper.DiscussPostMapper;
import cool.yzt.cap.service.DiscussPostService;
import cool.yzt.cap.service.LikeService;
import cool.yzt.cap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<DiscussPost> findAllOutsideBlacklistByPageNumber(int start, int limit) {
        PageHelper.startPage(start,limit);
        return discussPostMapper.selectAllOutsideBlacklist();
    }

    @Override
    public PageBean getPageBean(int start, int limit) {
        PageHelper.startPage(start,limit);
        List<DiscussPost> posts = discussPostMapper.selectAllOutsideBlacklist();
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
        PageBean pageBean = new PageBean();
        pageBean.setContents(contents);

        pageBean.setCurrent(pageInfo.getPageNum());
        pageBean.setSize(pageInfo.getPageSize());
        pageBean.setTotalContent(pageInfo.getTotal());
        pageBean.setTotalPage(pageInfo.getPages());
        pageBean.setPrePage(pageInfo.getPrePage());
        pageBean.setNextPage(pageInfo.getNextPage());
        pageBean.setFirst(pageInfo.isIsFirstPage());
        pageBean.setLast(pageInfo.isIsLastPage());

        return pageBean;
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
}
