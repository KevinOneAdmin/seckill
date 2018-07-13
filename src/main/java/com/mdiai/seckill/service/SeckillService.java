package com.mdiai.seckill.service;

import com.mdiai.seckill.common.codemsg.CodeMsg;
import com.mdiai.seckill.common.utils.MD5Util;
import com.mdiai.seckill.common.utils.StringUtils;
import com.mdiai.seckill.common.utils.UUIDUtils;
import com.mdiai.seckill.domain.OrderInfo;
import com.mdiai.seckill.domain.SeckillOrder;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.exception.GlodleException;
import com.mdiai.seckill.rabbitmq.SeckillMessage;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.redis.key.SeckillKey;
import com.mdiai.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * @author Kevin.Liu
 * @Date create in 2018/7/10  15:10
 * @Description 秒杀相关服务
 */
@Service
public class SeckillService {
    private static final Logger logger = LoggerFactory.getLogger(SeckillService.class);

    private static final String Seckill_PATH_SALT = "hY0sjXP";
    private static final char[] OPERATOR = {'+','-','*'};

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;


    /**
     * 异步下单
     * @param message
     */
    public void asynchronousOrder(SeckillMessage message) {
        Long goodsId = message.getGoodsId();
        SeckillUser user = message.getUser();

        //判断库存
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        if (1 > goods.getStockCount()) {
            return;
        }

        //判断重复秒杀
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (null != order) {
            return;
        }

        //减库存，下订单，写秒杀订单
        seckill(user, goods);
    }


    /**
     * 库存订单操作
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVO goods) {

        //乐观锁减库存
        int status = goodsService.reduceStock(goods);
        if (1 != status) {
            setGoodsOver(goods.getId());
            throw new GlodleException(CodeMsg.INVENTORY_REDUCTION_FAIL);
        }
        //下订单
        return orderService.createOrder(user, goods);
    }

    /**
     * 验证秒杀路径合法性
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(SeckillUser user, long goodsId, String path) {
        if(null == user || StringUtils.isEmpty(path)){
            return false;
        }
        String r_path = redisService.get(SeckillKey.SECKILLPATH, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(r_path);
    }


    /**
     * 创建秒杀动态路径
     * @param user
     * @param goodsId
     * @return
     */
    public String recatePath(SeckillUser user, long goodsId) {
        String path = MD5Util.md5(UUIDUtils.uuid() + Seckill_PATH_SALT);
        redisService.set(SeckillKey.SECKILLPATH,""+user.getId()+"_"+goodsId,path);
        return path;
    }


    /**
     * 生成验证码
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage crecateVerifyCod(SeckillUser user, long goodsId) {
        if(null == user || goodsId<=0){
            return null;
        }

        int width = 80;
        int height = 32;

        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = img.getGraphics();
        graphics.setColor(new Color(0xDCDCDC));
        graphics.fillRect(0,0,width,height);

        graphics.setColor(Color.black);
        graphics.drawRect(0,0,width-1,height-1);

        Random random = new Random();

        //生成干扰点
        for (int i=0;i<50;i++){
            int x =random.nextInt(width);
            int y = random.nextInt(height);
            graphics.drawOval(x,y,0,0);
        }

        //生成验证码
       String verifyCod = crecateVerifyCod(random);
       graphics.setColor(new Color(0,100,0));
       graphics.setFont(new Font("Candara",Font.BOLD,18));
       graphics.drawString(verifyCod,8,24);
       graphics.dispose();
       
       //把验证码存入Redis
       int rnd = calc(verifyCod);
       redisService.set(SeckillKey.VERIFYCOD,user.getId()+"_"+goodsId,rnd);

       logger.info("=========================图片验证码值:  "+rnd);

       //输出图片
       return img;

    }

    /**
     * 验证码公式
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
        if(null == user || goodsId<=0){
            return false;
        }
        Integer val = redisService.get(SeckillKey.VERIFYCOD, user.getId() + "_" + goodsId, Integer.class);
        if(null == val || val - verifyCode !=0){
            return false;
        }

        redisService.delete(SeckillKey.VERIFYCOD, user.getId() + "_" + goodsId);
        return true;
    }


    /**
     * 秒杀结果
     * @param userId
     * @param goodsId
     * @return
     */
    public long getResult(long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
        if (null != order) {
            return order.getGoodsId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 只提供 + - * 验证码
     * @param random
     * @return
     */
    private String crecateVerifyCod(Random random) {
        //计算数字
        int n1 = random.nextInt(100);
        int n2 = random.nextInt(100);
        int n3 = random.nextInt(100);

        //运算符
       char op1 = OPERATOR[random.nextInt(3)];
       char op2 = OPERATOR[random.nextInt(3)];

       String exp = "" +n1 + op1 + n2 + op2 + n3 ;
       return exp;
    }

    /**
     * 计算验证码的值
     * @param exp
     * @return
     */
    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            //获取JS-V8脚本引擎
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        }catch (Exception e){
            logger.error("calc exception:",e);
            return 0;
        }
    }






    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.ISGOODSOVER, "" + goodsId);
    }

    private void setGoodsOver(long goodsId) {
        redisService.set(SeckillKey.ISGOODSOVER, "" + goodsId, true);
    }


}
