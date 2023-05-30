package com.zhen.community;

import com.zhen.community.dao.*;
import com.zhen.community.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

//aa
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

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

    @Test
    public void testSelectPost(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0, 0, 10,0);
        System.out.println("bb"+discussPostList);
        for(DiscussPost post: discussPostList){
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);

    }



    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc",1);
        loginTicket =loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testInsertDiscussPost(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setId(149);
        discussPost.setTitle("测试");
        discussPost.setContent("test");
        discussPost.setCreateTime(new Date());
        int rows = discussPostMapper.insertDiscussPost(discussPost);

        System.out.println(rows);
        System.out.println(discussPost.getId());
    }

    @Test
    public void testSelectComment(){
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 12, 0, 3);
        //System.out.println("aa"+comments);
        for(Comment post: comments){
            System.out.println(post);
        }

        int rows = commentMapper.selectCountByEntity(1,12);
        System.out.println(rows);

    }

    @Test
    public void testSelectLetters(){
        List<Message> list = messageMapper.selectConversataions(111,0,20);
        for (Message message:list){
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112",0,10);
        for (Message message:list){
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count);
    }

}
