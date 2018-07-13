package com.mdiai.seckill.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Kevin.Liu
 * @Date create in 2018/7/9  14:33
 * @Description 3DESkey生成方案
 */
public class ThreeDESKeyUtils {
    private static final List<String> KEY_LIST = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "0", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");


    public static String createKey(int keyLength) {
        StringBuilder keybuf = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(KEY_LIST.size());
            keybuf.append(KEY_LIST.get(index));
        }
        return keybuf.toString();
    }

    public static void main(String[] args) {
        String key = createKey(7);
        System.out.println(key);
    }

}
