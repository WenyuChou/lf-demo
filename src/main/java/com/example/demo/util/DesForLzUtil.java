package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

/**
 * @Description:
 * @author 龙信提供
 * @date 2020/12/31
 * @param
 * @return
 */
@Slf4j
public class DesForLzUtil {


    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encryptString(String plainText, String secretKey) throws Exception {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            deskey = keyFactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);

            String tempText = plainText.charAt(plainText.length() - 1) + plainText + plainText.charAt(0);

            byte[] encryptData = cipher.doFinal(tempText.getBytes(encoding));
            return Base64.getEncoder().encodeToString(encryptData);
        } catch (Exception e) {
            log.error("加密异常，原因：{}", e.getMessage(), e);
            throw new Exception();
        }
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decryptString(String encryptText, String secretKey) throws Exception {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

            byte[] decryptData = cipher.doFinal(Base64.getDecoder().decode(encryptText));

            String result = new String(decryptData, encoding);

            return result.substring(1, result.length() - 1);
        } catch (Exception e) {
            log.error("解密异常，原因：{}", e.getMessage(), e);
            throw new Exception();
        }
    }

//    public static void main(String[] args) throws Exception {
//        String sec = "l6FkcCjEQ8uoLTWvrYzzixHqntljIB/U";
//        String a = AesForLzUtil.encryptString("lili1", "!>Gz%(DC$6s[1;?zIZ,U7wCgcOuX[,7&");
//        System.out.println(a);
////        String generateKey = DesForLzUtil.encryptString("zhangweizhong,123123132", a);
////        System.out.println(generateKey);
////        String b = DesForLzUtil.decryptString(generateKey, a);
////        System.out.println(b);
//        String b1 = DesForLzUtil.decryptString(sec, a);
//        System.out.println(b1);
//    }

}