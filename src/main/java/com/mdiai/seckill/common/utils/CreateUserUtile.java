package com.mdiai.seckill.common.utils;

import com.mdiai.seckill.domain.SeckillUser;

import java.util.Date;
import java.util.Random;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/11  10:47
 * @Description
 */
public class CreateUserUtile {

    private static final Random SUFFIX_PHONE_RANDOM = new Random();
    private static final Random PREFIX_PHONE_RANDOM = new Random();

    public static SeckillUser createUser(SeckillUser user) {

        int suffix = SUFFIX_PHONE_RANDOM.nextInt(99999999);
        int prefix = PREFIX_PHONE_RANDOM.nextInt(189);
        while (prefix < 130) {
            prefix = PREFIX_PHONE_RANDOM.nextInt(189);
        }

        String suffix_S = Integer.valueOf(suffix).toString();
        while (suffix_S.length() < 8) {
            suffix_S = "0" + suffix_S;
        }
        String nickName = ThreeDESKeyUtils.createKey(16);
        String solt = UUIDUtils.uuid();
        String pwd = ThreeDESKeyUtils.createKey(12);
        user.setId(Long.valueOf("" + prefix + suffix_S));
        user.setNickname(nickName);
        user.setSalt(solt);
        user.setPassword(MD5Util.formPassToDBPass(pwd, solt));
        user.setRegisterData(new Date());
        user.setLastLoginData(new Date());
        return user;
    }
}
