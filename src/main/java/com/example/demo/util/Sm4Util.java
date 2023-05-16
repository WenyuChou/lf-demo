package com.example.demo.util;

/**
 * @author zhouwenyu
 * date 2022-06-22
 */

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 国密SM4分组密码算法工具类（对称加密）
 * <p>GB/T 32907-2016 信息安全技术 SM4分组密码算法</p>
 * <p>SM4-ECB-PKCS5Padding</p>
 */
public class Sm4Util {
    //key必须是16字节，即128位cbfcb4fa642340ef
    final static String key = "8F3A0533700641EA";

    //指明加密算法和秘钥
    static SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());
    //加密为16进制，也可以加密成base64/字节数组
    public static String encryptSm4(String plaintext) {
        return sm4.encryptHex(plaintext);
    }

    //解密
    public static String decryptSm4(String ciphertext) {
        return sm4.decryptStr(ciphertext);
    }

    public static void main(String[] args) {
        System.out.println(String.join("_", "123", null, "456"));
        /*Stopwatch stopwatch = Stopwatch.createStarted();
        String content = "hello sm4";
        String plain = encryptSm4(content);
        String cipher = decryptSm4(plain);
        System.out.println(plain + "\n" + cipher);
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));*/
    }

}


