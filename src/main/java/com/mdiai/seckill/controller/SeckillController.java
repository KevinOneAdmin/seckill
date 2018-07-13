package com.mdiai.seckill.controller;

import com.alibaba.fastjson.util.IOUtils;
import com.mdiai.seckill.common.codemsg.CodeMsg;
import com.mdiai.seckill.common.access.AccessLimit;
import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.domain.OrderInfo;
import com.mdiai.seckill.domain.SeckillOrder;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.rabbitmq.MQSender;
import com.mdiai.seckill.rabbitmq.SeckillMessage;
import com.mdiai.seckill.redis.key.GoodsKey;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.service.GoodsService;
import com.mdiai.seckill.service.OrderService;
import com.mdiai.seckill.service.SeckillService;
import com.mdiai.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  13:34
 * @Description 商品
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();


    /**
     * 新的接口
     * 0表示处理中
     * 压测:线程=1000*10 QPS=2114
     *
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @AccessLimit(seconds = 60,maxCount = 30,needLogin = true)
    @RequestMapping(value = "/{path}/do_seckill", method = RequestMethod.POST)
    public Result<Integer> doSeckill(SeckillUser user, long goodsId,
                                     @PathVariable("path") String path) {
        if (null == user) {
            return Result.eorror(CodeMsg.USER_NOT_LOGIN);
        }

        //验证Path
        boolean flag = seckillService.checkPath(user,goodsId,path);
        if(!flag){
            return Result.eorror(CodeMsg.REQUEST_ILLEGAL);
        }

        //内存标志减少Redis缓存
        if (null != localOverMap.get(goodsId)) {
            return Result.eorror(CodeMsg.SECKILL_OVER);
        }

        //预减库存
        long stock = redisService.decr(GoodsKey.GETSECKILLGOODSSTOCK, "" + goodsId);
        if (0 > stock) {
            localOverMap.put(goodsId, true);
            return Result.eorror(CodeMsg.SECKILL_OVER);
        }

        // 判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (null != order) {
            return Result.eorror(CodeMsg.REPEATE_SECKILL);
        }

        //入队
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(message);

        return Result.success(0);
    }


    /**
     * 秒杀结果
     * orderId 成功
     * -1 秒杀失败
     * 0 处理中
     *
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getResult", method = RequestMethod.GET)
    public Result<Long> getResult(SeckillUser user, long goodsId) {
        long orderId = seckillService.getResult(user.getId(), goodsId);
        return Result.success(orderId);
    }


    /**
     * 获取秒杀路径并验证验证码
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @AccessLimit(seconds = 60,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/seckillPath", method = RequestMethod.GET)
    public Result<String> seckillPath(HttpServletRequest request,SeckillUser user, long goodsId,  int verifyCode) {

        boolean flag = seckillService.checkVerifyCode(user,goodsId,verifyCode);
        if(!flag){
            return Result.eorror(CodeMsg.REQUEST_ILLEGAL);
        }

        String path = seckillService.recatePath(user,goodsId);
        return Result.success(path);
    }




    /**
     * 生成验证码
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public Result<String> seckillVerifyCode(HttpServletResponse response,SeckillUser user, long goodsId) {
        BufferedImage img = seckillService.crecateVerifyCod(user,goodsId);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(img,"PNG",outputStream);
            outputStream.flush();
            return null;
        } catch (IOException e) {
            logger.error(" seckill create verifyCode exception:",e);
            return Result.eorror(CodeMsg.SECKILL_FAIL);
        }finally {
           IOUtils.close(outputStream);
        }
    }


    /**
     * 老的接口
     * 压测:线程=1000*10 QPS=1064
     *
     * @param user
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/old/do_seckill", method = RequestMethod.POST)
    public Result<OrderInfo> doSeckillOld(SeckillUser user, long goodsId) {
        if (null == user) {
            return Result.eorror(CodeMsg.USER_NOT_LOGIN);
        }
        //判断库存
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        if (1 > goods.getStockCount()) {
            return Result.eorror(CodeMsg.SECKILL_OVER);
        }

        //判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);

        if (null != order) {
            return Result.eorror(CodeMsg.REPEATE_SECKILL);
        }

        //减库存，下订单，写秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }

}
