package com.newcoder.community.controller;

import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.entity.Page;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.DiscussPostService;
import com.newcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @RequestMapping(path="/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page)
    {
        //方法调用前springmvc会自动实例化model和page，并将page注入model  所以在thymeleaf中可以直接访问page对象中数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");


        List<DiscussPost> list=discussPostService.findDiscussPosts(0,page.getOffSet(), page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(list!=null)
        {
            for(DiscussPost post:list)
            {
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User user=userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
}
