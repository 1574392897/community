package com.zhen.community.controller;

import com.zhen.community.Service.AlphaService;
import com.zhen.community.Service.DiscussPostService;
import com.zhen.community.Service.UserService;
import com.zhen.community.entity.DiscussPost;
import com.zhen.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


@Controller
@RequestMapping("/app")
public class app {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/app1")
    @ResponseBody
    public String app(){
        return "hello spring boot";
    }

    @RequestMapping("/app2")
    @ResponseBody
    public String getData(){
        return alphaService.Find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ':' + value);
        }
        System.out.println(request.getParameter("code"));

        //反回数据类型
        response.setContentType("text/html;charset=utf-8");
        try{
            PrintWriter writer = response.getWriter();
            writer.write("<h1>论坛<h1>");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //get请求
    //  /student?current=1&limit=10
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@RequestParam(name = "current",required = false,defaultValue = "1") int current,
                             @RequestParam(name = "current",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //post请求
    @RequestMapping(path = "student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }


    //响应html数据
    //方法1：
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","20");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //方法2：
    @RequestMapping(path = "/teacher1", method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","李四");
        model.addAttribute("age","30");
        return "/demo/view";
    }

    //响应json数据(异步请求)
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp(){
        Map<String, Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age","12");
        emp.put("salary","2000");
        return emp;
    }


}
