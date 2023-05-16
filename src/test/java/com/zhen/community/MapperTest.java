package com.zhen.community;

import com.zhen.community.dao.DiscussPostMapper;
import com.zhen.community.dao.UserMapper;
import com.zhen.community.entity.DiscussPost;
import com.zhen.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        User user1 = userMapper.selectByName("liubei");
        System.out.println(user1);

        User user2 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("1111");
        user.setEmail("nowcoder101@sina.com");
        user.setSalt("saa");
        user.setHeaderUrl("http://static.nowcoder.com/images/head/notify.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateStatus(){
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

    }

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectPost(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post: discussPostList){
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);

    }
}
