package com.zhen.community.service;

import com.zhen.community.dao.DemoDao;
import com.zhen.community.dao.DiscussPostMapper;
import com.zhen.community.dao.UserMapper;
import com.zhen.community.entity.DiscussPost;
import com.zhen.community.entity.User;
import com.zhen.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DemoService {
    @Autowired
    private DemoDao demoDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public String Find(){

        return demoDao.select();
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1(){
        // 新增用户
        User user = new User();
        user.setUsername("demo");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.Md5("123"+user.getSalt()));
        user.setEmail("asda@qq.com");
        user.setHeaderUrl("http://images.nowcoder.com/head/9t.pngg");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setId(user.getId());
        post.setTitle("新人报道");
        post.setContent("aasdad");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        // 人工干预报错，看看事务能不能回滚
        Integer.valueOf("cbc");

        return "ok";
    }
}
