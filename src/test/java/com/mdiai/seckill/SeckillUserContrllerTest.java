package com.mdiai.seckill;

import com.alibaba.fastjson.util.IOUtils;
import com.mdiai.seckill.common.utils.CreateUserUtile;
import com.mdiai.seckill.common.utils.ThreeDESUtils;
import com.mdiai.seckill.common.utils.UUIDUtils;
import com.mdiai.seckill.dao.SeckillUserDAO;
import com.mdiai.seckill.domain.SeckillUser;
import com.mdiai.seckill.redis.RedisService;
import com.mdiai.seckill.redis.key.SeckillUserKey;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/11  13:25
 * @Description 用户压测  线程shu:1000*10  QPS:4578.8
 */
public class SeckillUserContrllerTest extends BaseJunitTests {
    private static String LINE_SEPARATOR = System.getProperty("line.separator");


    @Autowired
    private SeckillUserDAO seckillUserDAO;

    @Autowired
    private RedisService redisService;

    /**
     * 用于压测产生10000个用户
     */
    @Ignore
    @Test
    public void createUserTest() {
        List<SeckillUser> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            SeckillUser seckillUser = new SeckillUser();
            SeckillUser user = CreateUserUtile.createUser(seckillUser);
            list.add(user);
        }
        int row = seckillUserDAO.batchInsert(list);
    }


    /**
     * 将token信息输出到TXT便于Jmeter压测,如果有特殊字符需要转意
     * @throws FileNotFoundException
     */
    @Ignore
    @Test
    public void writeTokenFileTest() throws FileNotFoundException {
        File file = new File("/token.txt");
        FileOutputStream fileOut = new FileOutputStream(file);
        List<SeckillUser> seckillUserList = seckillUserDAO.getSeckillUserList();
        seckillUserList.forEach(user -> {
            String token = UUIDUtils.uuid();
            long timeMillis = System.currentTimeMillis();
            long timeout = timeMillis + SeckillUserKey.TOKEN_EXPIRESECONDS * 1000;
            String c_token = token + "|" + timeout;
            String encrypt_token = ThreeDESUtils.encrypt(c_token, ThreeDESUtils.TOKEN_DES_KEY);
            String line = user.getId() + "|" + token  + LINE_SEPARATOR;
            try {
                redisService.set(SeckillUserKey.token, token, user);
                fileOut.write(line.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        IOUtils.close(fileOut);
    }


}
