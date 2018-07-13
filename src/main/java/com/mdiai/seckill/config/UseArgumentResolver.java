package com.mdiai.seckill.config;

import com.mdiai.seckill.common.access.UserConText;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  11:34
 * @Description 用户登录信息获取
 */
@Service
public class UseArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SeckillUserService seckillUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        return parameterType == SeckillUser.class?true:false;
    }

    /**
     * 获取用户信息
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
          return UserConText.getUser();
    }

}
