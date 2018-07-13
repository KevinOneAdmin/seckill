package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.domain.SeckillUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  13:34
 * @Description 用户信息
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ResponseBody
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Result<SeckillUser> info(SeckillUser user) {
        return Result.success(user);
    }
}
