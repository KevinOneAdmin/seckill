package com.mdiai.seckill.common.access;

import com.alibaba.fastjson.util.IOUtils;
import com.mdiai.seckill.common.codemsg.CodeMsg;
import com.mdiai.seckill.common.result.Result;
import com.mdiai.seckill.common.utils.JsonUtils;
import com.mdiai.seckill.common.utils.StringUtils;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.redis.key.AccessKey;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/13  11:40
 * @Description 限流拦截器
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private SeckillUserService seckillUserService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            SeckillUser user = getUser(request, response);
            UserConText.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit limit = hm.getMethodAnnotation(AccessLimit.class);
            if (null == limit) {
                return true;
            }
            int seconds = limit.seconds();
            int maxCount = limit.maxCount();
            boolean needLogin = limit.needLogin();
            String key = request.getRequestURI();

            if (needLogin) {
                if (null == user) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key = key + "_" + user.getId();
            }

            //查询访问次数
            AccessKey accessKey = AccessKey.whihExpire(seconds);
            Integer count = redisService.get(accessKey, key, Integer.class);
            if (null == count) {
                redisService.set(accessKey, key, 1);
            } else if (count < maxCount) {
                redisService.incr(accessKey, key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws Exception {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/json;charset=UTF-8");
            String msg = JsonUtils.beanToString(Result.eorror(sessionError));
            out.write(msg.getBytes("UTF-8"));
            out.flush();
        } finally {
            IOUtils.close(out);
        }

    }


    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SeckillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }

        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return seckillUserService.getByToken(response, token);
    }


    /**
     * 获取用户的token
     *
     * @param request
     * @param cookieNameToken
     * @return
     */
    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieNameToken)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
