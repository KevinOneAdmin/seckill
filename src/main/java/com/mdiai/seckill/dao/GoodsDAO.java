package com.mdiai.seckill.dao;

import com.mdiai.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  17:55
 * @Description
 */
@Mapper
public interface GoodsDAO {

    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods " +
            "g on sg.goods_id=g.id")
    List<GoodsVO>  getGoosVOList();

    @Select("select g.*,sg.seckill_price,sg.stock_count,sg.start_date,sg.end_date from seckill_goods sg left join goods " +
            "g on sg.goods_id=g.id where g.id = #{goodsId}")
    GoodsVO getGoodsVOByGoodsId(@Param("goodsId") long goodsId);

    @Update("update seckill_goods set stock_count = stock_count-1 where goods_id=#{id} and stock_count=stock_count and stock_count>0")
    int reduceStock(GoodsVO goods);
}
