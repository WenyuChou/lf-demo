package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 
* @Description:AES 加密工具类
* @author: zhengjie8
* @date: 2019年12月9日
 */
@Slf4j
public class AESUtil {
	
	private AESUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/GCM/PKCS5Padding";;// 默认的加密算法

	public static void main(String[] args) throws Exception{
		String sss ="PNuYbx9bK6X4fyjCwmwC2n8UzdGNSN1Vt6BOZDA22+iuAcBIjhMadZ8wHshWRK26YrwxOvfnRtWvHAmnA1k8DaxbsleY91/F8+2DDiHcGLPFayuYF7+rfwGOeIHjrSsjC4TVZnTWzq9ux9fz252VVGTaE14bLMOjgkgcZkyTWKzf5Pwr74Gvem5JGgtJd6YMlqsKCKE/Q9oCWg==";
		System.out.println(decrypt(sss,"b417b289-d6fb-4e70-8123-3f4b3ab7e3d6"));
	}

	/**
	 * AES 加密操作
	 *
	 * @param content     待加密内容
	 * @param password 加密密码
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content, String password) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
			byte[] iv = cipher.getIV();
			assert iv.length == 12;
			byte[] encryptData = cipher.doFinal(content.getBytes());
			assert encryptData.length == content.getBytes().length + 16;
			byte[] message = new byte[12 + content.getBytes().length + 16];
			System.arraycopy(iv, 0, message, 0, 12);
			System.arraycopy(encryptData, 0, message, 12, encryptData.length);
			return Base64.encodeBase64String(message);
		} catch (Exception e) {
			log.info("AESUtil#encrypt拋出异常,password:{},content:{}",password,content,e);
		}
		return null;
	}

	/**
	 * AES 解密操作
	 *
	 * @param base64Content
	 * @param password
	 * @return
	 */
	public static String decrypt(String base64Content, String password) {
		try {
			byte[] content = Base64.decodeBase64(base64Content);
			if (content.length < 12 + 16) {
				throw new IllegalArgumentException();
			}
			GCMParameterSpec params = new GCMParameterSpec(128, content, 0, 12);
			
				Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password), params);
				byte[] decryptData = cipher.doFinal(content, 12, content.length - 12);
				return new String(decryptData);
		} catch (Exception e) {
			log.info("AESUtil#decrypt拋出异常,password:{},content:{}",password,base64Content,e);
		}
		return null;
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKeySpec getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		
		SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptPass.getBytes());
		// 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
		kg.init(128, random);
		SecretKey secretKey = kg.generateKey();
		return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
	}
}