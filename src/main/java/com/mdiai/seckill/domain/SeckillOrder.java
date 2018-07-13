package com.mdiai.seckill.domain;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  17:50
 * @Description 秒杀订单
 */
public class SeckillOrder {

    private Long id;
    private Long userID;
    private Long orderId;
    private Long goodsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SeckillOrder{");
        sb.append("id=").append(id);
        sb.append(", userID=").append(userID);
        sb.append(", orderId=").append(orderId);
        sb.append(", goodsId=").append(goodsId);
        sb.append('}');
        return sb.toString();
    }
}
