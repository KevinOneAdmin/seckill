package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.domain.User;
import com.mdiai.seckill.rabbitmq.MQSender;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.redis.key.UserKey;
import com.mdiai.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Kevin.Liu
 * @Date create in 2018/7/5  13:40
 * @Description
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;



    @RequestMapping("/mq_send")
    @ResponseBody
    public Result<Boolean> mqSend(){
        mqSender.send("hello mq");
        return Result.success(true);
    }

    @RequestMapping("/mq_topic")
    @ResponseBody
    public Result<Boolean> mqTopic(){
        mqSender.sendTopic("hello topic mq");
        return Result.success(true);
    }

    @RequestMapping("/mq_fanout")
    @ResponseBody
    public Result<Boolean> mqFanout(){
        mqSender.sendFanout("hello fanout mq");
        return Result.success(true);
    }


    @RequestMapping("/mq_headers")
    @ResponseBody
    public Result<Boolean> mqHeaders(){
        mqSender.sendHeaders("hello headers mq");
        return Result.success(true);
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public Result<User> getUser(){
        User user = userService.getUser(1);
        return Result.success(user);
    }


    @RequestMapping("/tx")
    @ResponseBody
    public boolean tx(){
       return userService.tx();
    }

    @RequestMapping("/demo")
    public String thymeleaf(Model model){
        model.addAttribute("name","Kevin");
        return "hello";
    }


    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGetUser(){
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<User> redisSet(){
        User user = new User(1,"Kevin");
        boolean flag = redisService.set(UserKey.getById,"key1", user);
        User key1 = redisService.get(UserKey.getById,"key1", User.class);
        return Result.success(key1);
    }
}
