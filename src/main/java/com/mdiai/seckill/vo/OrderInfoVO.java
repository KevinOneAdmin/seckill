package com.mdiai.seckill.vo;

import com.mdiai.seckill.domain.OrderInfo;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/10  17:29
 * @Description
 */
public class OrderInfoVO extends OrderInfo {

    private String nickmane;

    private String goodsImg;



    public String getNickmane() {
        return nickmane;
    }

    public void setNickmane(String nickmane) {
        this.nickmane = nickmane;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    @Override
    public String toString() {
        return "OrderInfoVO{" +
                "nickmane='" + nickmane + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                '}';
    }
}
