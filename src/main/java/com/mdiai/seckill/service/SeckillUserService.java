package com.mdiai.seckill.service;

import com.mdiai.seckill.common.codemsg.CodeMsg;
import com.mdiai.seckill.common.utils.MD5Util;
import com.mdiai.seckill.common.utils.StringUtils;
import com.mdiai.seckill.common.utils.ThreeDESUtils;
import com.mdiai.seckill.common.utils.UUIDUtils;
import com.mdiai.seckill.dao.SeckillUserDAO;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.exception.GlodleException;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.redis.key.SeckillUserKey;
import com.mdiai.seckill.vo.LoginVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/6  17:43
 * @Description 秒杀用户服务
 */
@Service
public class SeckillUserService {
    private static final Logger logger = LoggerFactory.getLogger(SeckillUserService.class);
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SeckillUserDAO seckillUserDAO;

    @Autowired
    private RedisService redisService;

    public SeckillUser getById(long id) {
        //获取缓存
        SeckillUser user = redisService.get(SeckillUserKey.GETBYID, "" + id, SeckillUser.class);
        if (null != user) {
            return user;
        }
        //获取数据库
        user = seckillUserDAO.getByID(id);
        if (null != user) {
            redisService.set(SeckillUserKey.GETBYID, "" + id, user);
        }
        return user;
    }


    public boolean updateUserPwd(long id, String token, String pwd) {
        //获取用户
        SeckillUser user = getById(id);
        if (null == user) {
            throw new GlodleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新密码
        user.setPassword(MD5Util.formPassToDBPass(pwd, user.getSalt()));
        int update_flag = seckillUserDAO.updatePwd(id, user.getPassword());
        if (update_flag != 1) {
            throw new GlodleException(CodeMsg.PASSSWORD_UPDATE_FAIL);
        }
        //处理缓存
        redisService.delete(SeckillUserKey.GETBYID, "" + id);
        redisService.set(SeckillUserKey.token, token, user);
        return true;
    }

    public boolean login(HttpServletResponse response, LoginVO loginVO) {
        if (null == loginVO) {
            throw new GlodleException(CodeMsg.SERVICE_ERROR);
        }
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        //先判断手机号是否存在
        SeckillUser user = seckillUserDAO.getByID(Long.parseLong(mobile));
        if (null == user) {
            throw new GlodleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPassword = user.getPassword();
        String salt = user.getSalt();
        String passToDBPass = MD5Util.formPassToDBPass(password, salt);
        if (!passToDBPass.equals(dbPassword)) {
            throw new GlodleException(CodeMsg.PASSSWORD_ERROR);
        }


        //生成Coolie
        String token = UUIDUtils.uuid();
        addCookie(response, user, token);

        return true;
    }

    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        //TOKEN解密
        logger.info(">>>>>>>>>>>>>>>>>>" + token);
        String c_token = ThreeDESUtils.decrypt(token, ThreeDESUtils.TOKEN_DES_KEY);
        String[] tokenInfo = c_token.split("\\|");
        if(null == tokenInfo){
            logger.info(">>>>>>>>>>>>>>>>>>"+c_token);
        }
        String userToken = tokenInfo[0];
        Long timeOut = Long.valueOf(tokenInfo[1]);

        long timeMillis = System.currentTimeMillis();
        if (timeOut < timeMillis || timeOut - timeMillis > SeckillUserKey.TOKEN_EXPIRESECONDS * 1000) {
        //说明这个用户的TOKEN过期或者伪造
            return null;
        }


        //延长时间
        SeckillUser seckillUser = redisService.get(SeckillUserKey.token, userToken, SeckillUser.class);
        if (null == seckillUser) {
            return null;
        }
        addCookie(response, seckillUser, userToken);
        return seckillUser;
    }

    private void addCookie(HttpServletResponse response, SeckillUser user, String token) {
        long timeMillis = System.currentTimeMillis();
        long timeout = timeMillis + SeckillUserKey.TOKEN_EXPIRESECONDS * 1000;
        //生成防止篡改TOKEN
        String c_token = ThreeDESUtils.encrypt(token + "|" + timeout, ThreeDESUtils.TOKEN_DES_KEY);
        logger.info(">>>>>>>>>>>>>>>>>token:"+c_token);
        redisService.set(SeckillUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, c_token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
