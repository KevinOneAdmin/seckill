package com.mdiai.seckill.service;

import com.mdiai.seckill.dao.GoodsDAO;
import com.mdiai.seckill.redis.key.GoodsKey;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:43
 * @Description
 */
@Service
public class GoodsService implements InitializingBean {
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private GoodsDAO goodsDAO;

    @Autowired
    private RedisService redisService;

    public List<GoodsVO> listGoodsVO() {
        return goodsDAO.getGoosVOList();
    }


    public GoodsVO getGoodsVOByGoodsId(long goodsId) {
        return goodsDAO.getGoodsVOByGoodsId(goodsId);
    }

    public int reduceStock(GoodsVO goods) {
        //乐观锁减库存
        return goodsDAO.reduceStock(goods);
    }


    /**
     * 在系统初始化时调用
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsVOList = listGoodsVO();
        if (null == goodsVOList) {
            return;
        }
        goodsVOList.forEach(goodsVO -> {
            redisService.set(GoodsKey.GETSECKILLGOODSSTOCK, "" + goodsVO.getId(), goodsVO.getStockCount());
        });
    }
}
