package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.service.OrderService;
import com.mdiai.seckill.vo.OrderInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  13:34
 * @Description 商品
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;



    @ResponseBody
    @RequestMapping(value = "/getOrder",method = RequestMethod.GET)
    public Result<OrderInfoVO> getOrder(SeckillUser user,long orderId) {
        OrderInfoVO orderInfo =  orderService.getOrderInfoById(orderId);
        orderInfo.setNickmane(user.getNickname());
        return Result.success(orderInfo);
    }

}
