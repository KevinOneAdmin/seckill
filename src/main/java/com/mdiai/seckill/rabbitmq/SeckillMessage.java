package com.mdiai.seckill.rabbitmq;

import com.mdiai.seckill.domain.SeckillUser;

/**
 * @Auther Kevin.Liu
 * @Date create in 2018/7/12  14:37
 * @Description
 */
public class SeckillMessage {

    private SeckillUser user;

    private Long goodsId;

    public SeckillUser getUser() {
        return user;
    }

    public void setUser(SeckillUser user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
