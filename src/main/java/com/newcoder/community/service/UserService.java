package com.newcoder.community.service;


import com.newcoder.community.dao.LoginTicketMapper;
import com.newcoder.community.dao.UserMapper;
import com.newcoder.community.entity.LoginTicket;
import com.newcoder.community.entity.User;
import com.newcoder.community.util.CommunityConstant;
import com.newcoder.community.util.CommunityUtil;
import com.newcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

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

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id)
    {
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user)
    {
        Map<String,Object> map=new HashMap<>();

        if(user==null)
        {
            throw new IllegalArgumentException("参数不能为空！");
        }

        if(StringUtils.isBlank(user.getUsername()))
        {
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword()))
        {
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail()))
        {
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }

        //验证账号
        User u=userMapper.selectByName(user.getUsername());
        if(u !=null)
        {
            map.put("usernameMsg","账号已存在！");
            return map;
        }

        //验证邮箱
        u=userMapper.selectByEmail(user.getEmail());
        if(u !=null)
        {
            map.put("emailMsg","邮箱已存在！");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        System.out.println(user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        System.out.println(url);
        context.setVariable("url",url);
        System.out.println(context.toString());
        String content=templateEngine.process("/mail/activation",context);
        System.out.println(content);
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;

    }


    public int activation(int userId,String code)
    {
        User user=userMapper.selectById(userId);
        if(user.getStatus()==1)
        {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code))
        {
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else
        {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String,Object> login(String username, String password, long expiredSeconds)
    {
        Map<String,Object> map=new HashMap<>();

        //空值处理
        if(StringUtils.isBlank(username))
        {
            map.put("usernameMsg","账号不能为空" );
            return map;
        }
        if(StringUtils.isBlank(password))
        {
            map.put("passwordMsg","密码不能为空" );
            return map;
        }


        //验证账号
        User user=userMapper.selectByName(username);
        if(user==null)
        {
            map.put("usernameMsg","账号不存在" );
            return map;
        }

        //验证账号状态
        if(user.getStatus()==0)
        {
            map.put("usernameMsg","账号未激活" );
            return map;
        }

        //验证密码
        password=CommunityUtil.md5(password+user.getSalt());
        if(!user.getPassword().equals(password))
        {
            map.put("passwordMsg","密码错误" );
            return map;
        }

        //生成登陆凭证
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket)
    {
        loginTicketMapper.updateStatus(ticket,1);
    }


}
