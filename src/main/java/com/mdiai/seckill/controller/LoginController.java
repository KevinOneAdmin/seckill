package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.service.SeckillUserService;
import com.mdiai.seckill.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  13:34
 * @Description
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SeckillUserService seckillUserService;


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,@Validated LoginVO loginVO) {
        logger.info(loginVO.toString());
        seckillUserService.login(response,loginVO);
        return Result.success(true);
    }

}
