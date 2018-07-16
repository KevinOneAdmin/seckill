package com.mdiai.seckill.controller;

import com.mdiai.seckill.common.utils.StringUtils;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.redis.key.GoodsKey;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.service.GoodsService;
import com.mdiai.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/11  17:02
 * @Description 模板引擎页面Redis缓存
 */
@Controller
@RequestMapping("/goods")
public class GoodsRedisController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisService redisService;


    /**
     * QPS=2884
     * Thymeleaf加Redis缓存.
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/redis/to_list",produces = "text/html")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user) {
        //取缓存
        String html = redisService.get(GoodsKey.GETGOODSLIST, "", String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
        //手动渲染
        List<GoodsVO> goodsList = goodsService.listGoodsVO();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("user",user);
        SpringWebContext springWebContext = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        String goods_list_html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);

        if (!StringUtils.isEmpty(goods_list_html)){
            redisService.set(GoodsKey.GETGOODSLIST,"",goods_list_html);
        }
        return "goods_list";
    }
}
