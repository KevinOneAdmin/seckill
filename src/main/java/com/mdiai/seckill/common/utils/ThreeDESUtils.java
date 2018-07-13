package com.mdiai.seckill.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  13:51
 * @Description 3DES加解密工具
 */
public class ThreeDESUtils {
    private static final Logger logger = LoggerFactory.getLogger(ThreeDESUtils.class);
    private static final String Algorithm = "DESede";
    public static final String TOKEN_DES_KEY = "dnLa4bk1FvtildVjQxCW75Bm";



    /**
     * 加密
     *
     * @param srcStr
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String srcStr, String key)  {

        try {
            byte[] keybyte = toHex(key);
            byte[] src = srcStr.getBytes();
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return Base64.encodeBase64String(c1.doFinal(src));
        } catch (Exception e){
            logger.error("3DES加密失败:",e);
        }
        return null;
    }


    /**
     * 解密
     *
     * @param desStr
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String desStr, String key)  {
        try {
            Base64 base64 = new Base64();
            byte[] keybyte = toHex(key);
            byte[] src = base64.decode(desStr);
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return new String(c1.doFinal(src));
        }catch (Exception e){
            logger.error("3DES解密失败:",e);
        }
        return null;
    }


    private static byte[] toHex(String keyStr){
        String f = DigestUtils.md5Hex(keyStr);
        byte[] bkeys = new String(f).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }


    public static void main(String[] args) throws Exception {
//        String in = "yslgfldRZnHEIQozTouAB0i1ohqWjlsE|"+System.currentTimeMillis();
//        System.out.println(in);
//        String encrypt = encrypt(in, TOKEN_DES_KEY);
//        System.out.println(encrypt);
//        String decrypt = decrypt(encrypt, TOKEN_DES_KEY);
//        System.out.println(decrypt);
    }

}
