package com.newcoder.community.controller;

import com.newcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class Alphacontroller {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello()
    {
        return "hello spring ";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData()
    {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response)
    {
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration=request.getHeaderNames();
        while(enumeration.hasMoreElements())
        {
            String name=enumeration.nextElement();
            String value=request.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset-utf-8");
        try(PrintWriter writer=response.getWriter();) {

            writer.write("<h1>test</h1>");
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    //get请求 两种传参方式
    // /students?current=1&limit=20
    @RequestMapping(path ="/students",method = RequestMethod.GET)
    @ResponseBody
    public String getstudents(
            @RequestParam(name="current",required = false,defaultValue = "1") int current,
            @RequestParam(name="limit",required = false,defaultValue = "10") int limit)
    {
        System.out.println(current);
        System.out.println(limit);
        return "sstudents";
    }

    // /students/123
    @RequestMapping(path="/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id)
    {
        System.out.println(id);
        return "id student";
    }

    //post请求
    @RequestMapping(path="/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age)
    {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应HTML数据
    @RequestMapping(path="/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher()
    {
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","zyj");
        mav.addObject("age",30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path="/school",method = RequestMethod.GET)
    public String getSchool(Model model)
    {

        model.addAttribute("name","cauc");
        model.addAttribute("age",70);
        return "/demo/view";
    }

    //响应json数据  异步请求 注册时判断是否名字呗占用 页面没有刷新
    //java对象 -> json -> js对象
    @RequestMapping(path="/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp()
    {
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","qqw");
        emp.put("age",23);
        emp.put("salary",8000.00);
        return emp;
    }

    @RequestMapping(path="/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps()
    {
        List<Map<String,Object>> list=new ArrayList<>();

        Map<String,Object> emp=new HashMap<>();
        emp.put("name","qqw");
        emp.put("age",23);
        emp.put("salary",8000.00);
        list.add(emp);

        emp.put("name","qaz");
        emp.put("age",25);
        emp.put("salary",6000.00);
        list.add(emp);

        emp.put("name","wsx");
        emp.put("age",28);
        emp.put("salary",9000.00);
        list.add(emp);


        return list;
    }

}
