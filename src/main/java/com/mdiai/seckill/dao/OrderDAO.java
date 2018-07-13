package com.mdiai.seckill.dao;

import com.mdiai.seckill.domain.OrderInfo;
import com.mdiai.seckill.domain.SeckillOrder;
import com.mdiai.seckill.vo.OrderInfoVO;
import org.apache.ibatis.annotations.*;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/10  15:14
 * @Description
 */
@Mapper
public interface OrderDAO {

    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info( " +
            "user_id\n" +
            ",goods_id\n" +
            ",delivery_addr_id\n" +
            ",goods_name\n" +
            ",goods_count\n" +
            ",goods_price\n" +
            ",order_channel\n" +
            ",status\n" +
            ",create_date)" +
            "values\n"+
              "(#{userId}" +
            ",#{goodsId}" +
            ",#{deliveryAddrId}" +
            ",#{goodsName}" +
            ",#{goodsCount}" +
            ",#{goodsPrice}" +
            ",#{orderChannel}" +
            ",#{status}" +
            ",#{createDate})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into seckill_order (user_id , order_id , goods_id) values (#{userID},#{orderId},#{goodsId})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("select * from order_info where id=#{orderId}")
    OrderInfoVO getOrderInfoById(@Param("orderId") long orderId);
}
