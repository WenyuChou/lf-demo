package com.example.demo.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


public class RsaCryptionUtils {

    // RSA一次加密最大输入长度
    private static final int MAX_ENCRYPT_BLOCK  = 117;

    // RSA一次解密最大输入长度，该长度=密钥长度/8，因此128意味着密钥长度为1024
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String ENCODING_UTF8 = "UTF-8";

    private static final String ALGO_RSA = "RSA";

    private static boolean isEmptyOrNull(String str) {
        return (str == null || str.equals("")) ;
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encryptByPublicKey(String str, String publicKey){
        if (isEmptyOrNull(str)) return null;

        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);

        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGO_RSA).generatePublic(new X509EncodedKeySpec(decoded));

            Cipher cipher = Cipher.getInstance(ALGO_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] toBeEncryptedBytes = str.getBytes(ENCODING_UTF8);
            byte[] resultBytes = {};
            byte[] segBytes;

            int offset = 0, size;

            // 每次加密最大长度最大为117个字节，因此循环加密
            while (offset < toBeEncryptedBytes.length) {
                size = toBeEncryptedBytes.length - offset;
                if (size > MAX_ENCRYPT_BLOCK) size = MAX_ENCRYPT_BLOCK;

                segBytes = cipher.doFinal(ArrayUtils.subarray(toBeEncryptedBytes, offset, offset + size));
                resultBytes = ArrayUtils.addAll(resultBytes, segBytes);

                offset += size;
            }


            return Base64.encodeBase64String(resultBytes);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decryptByPrivateKey(String str, String privateKey){
        if (isEmptyOrNull(str)) return null;

        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);

        try {
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGO_RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));

            //RSA解密
            Cipher cipher = Cipher.getInstance(ALGO_RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            byte[] toBeDecodedBytes = Base64.decodeBase64(str.getBytes("UTF-8"));
            byte[] resultBytes = {};
            byte[] segBytes;

            int offset = 0, size;

            while (offset < toBeDecodedBytes.length) {
                size = toBeDecodedBytes.length - offset;
                if (size > MAX_DECRYPT_BLOCK) size = MAX_DECRYPT_BLOCK;

                segBytes = cipher.doFinal(ArrayUtils.subarray(toBeDecodedBytes, offset, offset + size));
                resultBytes = ArrayUtils.addAll(resultBytes, segBytes);

                offset += size;
            }

            return new String(resultBytes);

        } catch(Exception e) {
            return null;
        }
    }
    //我方公钥,给对方
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsPMT_jAuhbtMwDKgbCwX7j8E0I6jfSXhQN70_Y-ce2bMoenr00txy0-d3iip-AJEuD7OUmndk_11MEbe10gjPD5vwlXAR8clh9nnGtMvl_BUVwUfYCf6jirtx_m4ChuK2Jon00XicFqpD1orPo3usCFhhzAiM-FUH9dLcvjWfUQIDAQAB";
    //我方私钥
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKw8xP-MC6Fu0zAMqBsLBfuPwTQjqN9JeFA3vT9j5x7Zsyh6evTS3HLT53eKKn4AkS4Ps5Sad2T_XUwRt7XSCM8Pm_CVcBHxyWH2eca0y-X8FRXBR9gJ_qOKu3H-bgKG4rYmifTReJwWqkPWis-je6wIWGHMCIz4VQf10ty-NZ9RAgMBAAECgYBkLtTHb8Y0GCpvAG2njTnloTBYC4Xmmu2Ie2Nww8Njhm1XVia-0Qe5Iti4B32_3MSVbjyea1qXPJIf9Q5Oi-Ti13IIG1TkbZAuHt6fTSDjlLajnLT2dn2QOnALrTqltXBStplBmFoVhWK6_fYfrU3ATn0HydDpO6RfJZWLUVG5gQJBANbGmpm4gyvEjWLjNvmvpMBrhhEQCoUyyOCqt7y8-ZThP4yo1LBWI3ykHzVYGWOJ-T15A1voghg6Ux99PJqc47UCQQDNS_a_FOE5mrLX8sQWcRT6zSAGcV_zOmAQTWVV50cNCur6EwjozloW4R-8beELr9j2aGEhyg_XXPwChMpEY4atAkB5YuYd21rHcd829JQtqkfDOQIcwqQvg_RkhhgyY9vreSMhrIoI36ZO27OHwrMWvvRO-gYb-90LNGumcHHAMMvBAkA92hIoNcE5mrshRrn_TOiWO3DXoBbZcA_7OWY7gGeurUhoOY3onisUU5U9LxmrbLqAWqhRBtDJAPHITHuoKRgtAkBkZeuBIvpvvrAHtVQTVKgwfx9DhI3RjHDCJaB64Im0VEcZz3v7ZOmI8fXrvjLUPzEQZYr6WnNsrHzwpudhTL8i";
    //对方公钥
    private static String shuangHuPublicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCEPJwOeNXvFakCCifs0TRXC4wooAoSUteyFc6slnviSWMmxnRMmTtU9qfyR+jbrLOo9CyLKta485Q+33rO5D7MC1tqWNrnsjBH1is39qqK1gXR1hkxP9ruOXL+YADesEYCa/MI93h8FHFU39ux2nXmRRdjrGRFplFBoj/zuLAE8wIBAw==";
    //双湖加密
    public static String shuangHuRsaEncrypt(String param){
        return RsaCryptionUtils.encryptByPublicKey(param, shuangHuPublicKey);
    }
    //双湖返回数据解密
    public static String shuangHuRsaDecrypt(String result){
        return RsaCryptionUtils.decryptByPrivateKey(result, privateKey);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCI/IU1+HPD1T9RwyBbsu0UN67bG6IwElhL7WXLtQ9LwEo7Ti2Vd0uPrv69PKmZKjgBCj6Q1eA2C9gH350NP4G3zhsoor9mqNewnOj3V2JdkKvdA+c2wFOnWSorrfbgyaMO2Wd6A54kcUHth5UPiEvD+5PlHbNNcGaFhsyAw9aQVQIBAw==";
        String privateKey = "MIICdAIBADANBgkqhkiG9w0BAQEFAASCAl4wggJaAgEAAoGBAIj8hTX4c8PVP1HDIFuy7RQ3rtsbojASWEvtZcu1D0vASjtOLZV3S4+u/r08qZkqOAEKPpDV4DYL2AffnQ0/gbfOGyiiv2ao17Cc6PdXYl2Qq90D5zbAU6dZKiut9uDJow7ZZ3oDniRxQe2HlQ+IS8P7k+Uds01wZoWGzIDD1pBVAgEDAoGAFtTA3lQTS041OEswD0h82LPyednwXVhkDKeQ90jX4fVhtI0HmOk3Qp0qdN9xmYcJVYG1GCOlXldOq/qaLN/q87jCaMhP89EV29Cjjnb6wmxqgrKpO6P48KrfbJAUHPVNh22rV85q0dh+0P56WHVORLnc2b46vM/+Ghwb+Gwb+KsCQQDUmFvdOjijSmgB1wYbAoH8t2d57jQ/+ytE5aKFqJ9cG7rMtTFSxArHTM6q/fQRyYKRDIgAmDXpxEf9rtJnZqEBAkEApPRYE6V3Hwohtzyacn9NCXVlWgGcqILYEwf9x9WTrbYneq452lkulvw45bkItqylD1pCp7qqpq2g4HXb08gbVQJBAI265+jRexeG8AE6BBIBq/3PmlFJeCqnci3ubFkbFOgSfIh4y4yCsdozNHH+oraGVwtdsABleUaC2qkfNu+ZwKsCQG34OrfDpL9cFnooZvb/iLD47jwBExsB5WIFU9qOYnPOxPx0JpGQybn9e0PQsHnIbgo8LG/RxxnJFer5PTfavOMCQFcBAS6npLdG2z6pUn7Dt2H7VLLAfVb2BQGUocBhxK5+k4sWfug+QVMlTjxGFrwX6LOEGstXHAUgXlS8QQkK9Bc=";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsPMT_jAuhbtMwDKgbCwX7j8E0I6jfSXhQN70_Y-ce2bMoenr00txy0-d3iip-AJEuD7OUmndk_11MEbe10gjPD5vwlXAR8clh9nnGtMvl_BUVwUfYCf6jirtx_m4ChuK2Jon00XicFqpD1orPo3usCFhhzAiM-FUH9dLcvjWfUQIDAQAB";
        privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKw8xP-MC6Fu0zAMqBsLBfuPwTQjqN9JeFA3vT9j5x7Zsyh6evTS3HLT53eKKn4AkS4Ps5Sad2T_XUwRt7XSCM8Pm_CVcBHxyWH2eca0y-X8FRXBR9gJ_qOKu3H-bgKG4rYmifTReJwWqkPWis-je6wIWGHMCIz4VQf10ty-NZ9RAgMBAAECgYBkLtTHb8Y0GCpvAG2njTnloTBYC4Xmmu2Ie2Nww8Njhm1XVia-0Qe5Iti4B32_3MSVbjyea1qXPJIf9Q5Oi-Ti13IIG1TkbZAuHt6fTSDjlLajnLT2dn2QOnALrTqltXBStplBmFoVhWK6_fYfrU3ATn0HydDpO6RfJZWLUVG5gQJBANbGmpm4gyvEjWLjNvmvpMBrhhEQCoUyyOCqt7y8-ZThP4yo1LBWI3ykHzVYGWOJ-T15A1voghg6Ux99PJqc47UCQQDNS_a_FOE5mrLX8sQWcRT6zSAGcV_zOmAQTWVV50cNCur6EwjozloW4R-8beELr9j2aGEhyg_XXPwChMpEY4atAkB5YuYd21rHcd829JQtqkfDOQIcwqQvg_RkhhgyY9vreSMhrIoI36ZO27OHwrMWvvRO-gYb-90LNGumcHHAMMvBAkA92hIoNcE5mrshRrn_TOiWO3DXoBbZcA_7OWY7gGeurUhoOY3onisUU5U9LxmrbLqAWqhRBtDJAPHITHuoKRgtAkBkZeuBIvpvvrAHtVQTVKgwfx9DhI3RjHDCJaB64Im0VEcZz3v7ZOmI8fXrvjLUPzEQZYr6WnNsrHzwpudhTL8i";
        String s = "[{\"serialNumber\":\"150955928525626\",\"employeeOa\":\"ceshi01\",\"employeeName\":\"测试01\",\"amount\":\"1800000\",\"currencyType\":\"2020/4/1 17:39:06\",\"tradingUnit\":\"1\",\"issueDate\":\"2021-07-05\",\"systemCode\":\"100000\",\"welfareProject\":\"车辆补贴\"},{\"SerialNumber\":\"150955928525626\",\"employeeOa\":\"ceshi02\",\"employeeName\":\"测试02\",\"amount\":\"1800000\",\"currencyType\":\"2020/4/1 17:39:06\",\"tradingUnit\":\"1\",\"issueDate\":\"2021-07-05\",\"systemCode\":\"100000\",\"welfareProject\":\"车辆补贴\"}]";

        String encrypt = RsaCryptionUtils.encryptByPublicKey(s, publicKey);
        System.out.println(encrypt);

        String decrypt = RsaCryptionUtils.decryptByPrivateKey(encrypt, privateKey);
        System.out.println(decrypt);

        Map<String, String> keys = RSAUtils.createKeys(1024);
        System.out.println(keys.get("publicKey"));
        System.out.println(keys.get("privateKey"));
    }


}
