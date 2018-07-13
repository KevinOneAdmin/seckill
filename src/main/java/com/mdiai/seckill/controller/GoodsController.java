package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.service.GoodsService;
import com.mdiai.seckill.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  13:34
 * @Description 商品
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsService goodsService;

    /**
     * 压测:线程=1000*10 QPS=1247
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/to_list",method = RequestMethod.GET)
    public Result<List<GoodsVO>> toList() {
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        return Result.success(goodsList);
    }


    @ResponseBody
    @RequestMapping(value = "/to_detail",method = RequestMethod.GET)
    public Result<GoodsVO> toDetail(long goodsId) {
        GoodsVO goods = goodsService.getGoodsVOByGoodsId(goodsId);
        return Result.success(goods);
    }

}
