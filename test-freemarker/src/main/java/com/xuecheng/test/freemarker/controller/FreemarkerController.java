package com.xuecheng.test.freemarker.controller;

/**
 * @author xxm
 * @create 2019-03-05 20:20
 */

import java.util.*;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @RequestMapping("/test1")
    public String freemarker(Map<String, Object> map) {
        // map就是freemarker模板使用的数据
        //向数据模型放数据
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new  Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        // 给第二名同学设置朋友列表
        stu2.setFriends(friends);
        // 给第二名同学设置最好朋友
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        //向数据模型放数据
        map.put("stu1", stu1);
        //向数据模型放数据
        map.put("stuMap", stuMap);
        //返回模板文件名称
        //返回模板文件名称
        return "test1";
    }

    //@RequestMapping("/test1")
    //public String freemarker(Model model) {
    //    // map就是freemarker模板使用的数据
    //    model.addAttribute("name","呵呵哒");
    //    //返回模板文件名称
    //    return "test1";
    //}
    //@RequestMapping("/test1")
    //public String freemarker(HttpServletRequest request) {
    //    // map就是freemarker模板使用的数据
    //    request.setAttribute("name","呵呵哒");
    //    //返回模板文件名称
    //    return "test1";
    //}
}