package com.mdiai.seckill.service;

import com.mdiai.seckill.dao.OrderDAO;
import com.mdiai.seckill.domain.OrderInfo;
import com.mdiai.seckill.domain.SeckillOrder;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.redis.key.OrderKey;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.vo.GoodsVO;
import com.mdiai.seckill.vo.OrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/10  15:04
 * @Description
 */
@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;

    public SeckillOrder getSeckillOrderByUserIdAndGoodsId(long userId, long goodsId) {
        SeckillOrder seckillOrder = redisService.get(OrderKey.getSeckillOrder, "" + userId + "_" + goodsId, SeckillOrder.class);
        if(null == seckillOrder){
            seckillOrder = orderDAO.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
            redisService.set(OrderKey.getSeckillOrder, "" + userId + "_" + goodsId, seckillOrder);
        }
        return seckillOrder;
    }

    @Transactional
    public OrderInfo createOrder(SeckillUser user, GoodsVO goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setUserID(user.getId());

        orderDAO.insert(orderInfo);
        seckillOrder.setOrderId(orderInfo.getId());
        long seckillOrderId = orderDAO.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrder, "" + user.getId() + "_" + goods.getId(), seckillOrder);
        return orderInfo;
    }

    public OrderInfoVO getOrderInfoById(long orderId) {
        OrderInfoVO order = orderDAO.getOrderInfoById(orderId);
        GoodsVO godes = goodsService.getGoodsVOByGoodsId(order.getGoodsId());
        order.setGoodsImg(godes.getGoodsImg());
        return order;
    }
}
