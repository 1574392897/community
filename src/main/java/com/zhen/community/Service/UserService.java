package com.zhen.community.Service;

import com.zhen.community.dao.LoginTicketMapper;
import com.zhen.community.dao.UserMapper;
import com.zhen.community.entity.LoginTicket;
import com.zhen.community.entity.User;
import com.zhen.community.util.CommunityConstant;
import com.zhen.community.util.CommunityUtil;
import com.zhen.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }



    //注册功能 需要用到邮件注册
    /*
        1.需要用到springboot使用 TemplateEngine模板 发送HTML邮箱验证码
        2.发送激活码中包含：域名  项目名  要注入来 因为是一个值不是bean 所以用到 @value注解
    */
    //声 明变量domain 接收 @Value 中的值
    // 获取的路径 域名
    @Value("@{community.path.domain}")
    private String domain;

    // 获取的项目名
    @Value("@{server.servlet.context-path}")
    private String contextPath;

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        //空值异常
        if(user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsq","账号为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsq","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsq","密码不能为空");
            return map;
        }

        // 验证账号是否已经存在
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsq","该账号已经存在");
            return map;
        }

        // 验证邮箱是否存在
        User byEmail = userMapper.selectByEmail(user.getEmail());
        if(byEmail != null){
            map.put("emailMsq","该邮箱已经注册");
            return map;
        }

        // 注册用户 密码加密   加密用到 md5+随机字符串
        //生成随机字符串只生成 0-5个字符串
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        //加密密码
        user.setPassword(CommunityUtil.Md5(user.getPassword()+user.getSalt()));
        //注册类型
        user.setType(0);
        user.setStatus(0);
        //发送激活码 这个可以长一点
        user.setActivationCode(CommunityUtil.generateUUID());
        //生成一个随机的头像
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // 希望 服务器 用什么URL路径去处理这个请求，可以自己定义： http://localhost:8080/community/activation/101/code   101是用户名，code是激活码
        String url = domain + contextPath +"/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        //利用模版引擎 TemplateEngine 生成邮件内容  其中/mail/activation  指的是activation.html文件
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }

    // 注册功能
    // 激活账号
    /*1。成功激活
    * 2.点击激活多次，只取激活第一次
    * 3.验证码是伪造的，激活失败*/
    public int activation(int userId,String code){
       User user = userMapper.selectById(userId);
       if(user.getStatus() == 1){
           return activation_repeat;
       }else if(user.getActivationCode().equals(code)){
           userMapper.updateStatus(userId,1);
           return activation_sussess;
       }else {
           return activation_falluer;
       }
    }


    // 登录功能   expiredSeconds:声明过期时间
    public Map<String, Object> login(String username,String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        // 空值判断
        if (StringUtils.isBlank(username)){
            map.put("usernameMsq","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsq","密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsq","该账号不存在");
            return map;
        }
        // 验证密码
        String password1 = CommunityUtil.Md5(password + user.getSalt());
        if (!user.getPassword().equals(password1)){
            map.put("passwordMsq","密码不正确");
            return map;
        }
        // 验证状态
        if (user.getStatus() == 0){
            map.put("statusMsq","该账号未激活");
            return map;
        }

        // 登录数据正确的话， 就会生成一个登录验证凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    // 将凭证改为失效的状态
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

}
