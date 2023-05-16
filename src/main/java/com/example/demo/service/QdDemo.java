package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.util.AESUtil;
import com.example.demo.util.HttpClient;
import com.example.demo.util.RSAUtils;
import com.example.demo.util.WalletApiParamEncryptReq;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhouwenyu
 * date 2021-12-15
 */
@Slf4j
public class QdDemo {
    //url
    private final String lzUrl = "http://api.longfor.sit/lf-wallet-trading-endpoint-sit/api/grantQd/singleGrant";
    //珑珠公钥
    private final String lzPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpOEb5vGe054rcK_aWfMGrfFS_YQ_ReKMtjdncv9NSWSor16NEyJSYV18BFtoTVg8ShL9K7u3k21fjVEHoTzu6Y4B35DBnuLkw7SvKcPodp0333GJu6r6TIw3hBMbw8z86cGo9OR9WIeNvjsZyRYMubKRHKJ6gdJGmLG2gV7iDwQIDAQAB";
    //网关gaiaKey
    private final String gaiaKey = "2b19d848-fcf6-49ba-8c5f-d07dfcaf4b3e";
    //千丁appId
    private final String qdAppId = "146";
    //千丁activityType
    private final String activityType = "20294";
    //千丁公钥
    private final String qdPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFEFedBSkQ4M0oGFdSBPqJ3Oj9W7DoHnV0Ljlt5T1RJ0UlAQNW40HmlHzvac9qA3B46_Dn2ejS8J7zz4io2W061HR0Dz8fbupukAqgezIlHaH1mJctQAh3CjalcX7-wlppDfVT5e3nUplCI9oBrl8i-9zLk49XPoyKJb2XFCCRgwIDAQAB";
    //千丁私钥
    private final String qdPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIUQV50FKRDgzSgYV1IE-onc6P1bsOgedXQuOW3lPVEnRSUBA1bjQeaUfO9pz2oDcHjr8OfZ6NLwnvPPiKjZbTrUdHQPPx9u6m6QCqB7MiUdofWYly1ACHcKNqVxfv7CWmkN9VPl7edSmUIj2gGuXyL73MuTj1c-jIolvZcUIJGDAgMBAAECgYBlmvv7t2QLMk0Vz7hltc2Flf0G8YvdQtzXETDjHIe8vyrKPcLKBxBcPIhF3Fr4DZZFj4xNzhQ9dmU5sQAeRqBjjeKdO5n-1RGN93VzJxCjFsjmyuBvPRAMYmeUU_PtRJwrn9J-6cO1HmMyWlu7X2MuGtUKcbejSm1Cgp-xADYUmQJBANVNS2SrGl8r3OSul9F6UdNDldvvUlxwDG_asaDICrXLbkwmy9lto4_if89bEHI7-sVfPvd6OUXsMdMKsJ_Q158CQQCfszp_ihvPbIpJmSbjbu__QZ8QaDoC1woQbB0sFUYMpKsgrhmSLHI4UegHeZZxT6kIqgSL0x7kRMAhIKw1Z4udAkEArWwetEZ5No_PFQ7u2J3_ESmOGjdGW2mucvL4IKUEMDKqBrg0XR9LMJ0TFWAQyxABH6AE7ektz6_uAoxadlWyOQJALiy_PEMteVLzxQXwNZBM-iv6Ft9b7phIg6kO4kEgaOVvyVotcJNDTz93bDneotKamDO4bslTREMQnkN5J1PSDQJBAJZ6aGmjcHKV6Y02P4IkvjpX_0m3aeaiazzedCDiy0-7GKmAb3TwvHg01HHsfsb8NqkEe9fZpx3Mi18aASIB_b0";

    public static void main(String[] args) {
        new Thread(() -> new QdDemo().grant()).start();
        //new Thread(() -> new QdDemo().grant()).start();
    }
    /**
     * 千丁发放
     */
    public void grant() {
        Map<String, Object> param = new HashMap<>();
        param.put("appId", this.qdAppId);
        param.put("outTransNo", "qdtst20211214100016");
        param.put("serialNumber", "qdfk0000001");
        param.put("accountNo", "FF-211216-16549");
        param.put("activityType", this.activityType);
        param.put("grantAmt", "10");
        //caihenian
        param.put("personOa", "liuqing21");
        param.put("remark", "千丁发放");
        param.put("timestamp", System.currentTimeMillis());
        param.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        WalletApiParamEncryptReq walletApiParamEncryptReq = new QdDemo().apiParamEncryptReq(param);
        System.out.println(JSON.toJSONString(walletApiParamEncryptReq));
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("X-Gaia-Api-Key", this.gaiaKey);
        String result = HttpClient.doHttp(this.lzUrl, JSON.toJSONString(walletApiParamEncryptReq), head, "POST", 10)[1];
        System.out.println(System.currentTimeMillis());
        System.out.println(new QdDemo().walletResponseDecrypt(JSONObject.parseObject(result)));
    }

    //调用钱包（原始入参加签，加密）
    private WalletApiParamEncryptReq apiParamEncryptReq(Map<String, Object> originalParamMap) {

        try {
            /**原始参数生成签名，并添加签名到原始参数*********************************************************/
            //原始参数转JSON字符串
            String originalParamJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);
            log.info("originalParamJsonStr info = {}", originalParamJsonStr);

            //原始参数JSON串，私钥生成签名
            String sign = RSAUtils.sign(originalParamJsonStr, qdPrivateKey);

            //生成的签名sign添加到原始参数中
            originalParamMap.put("sign", sign);

            //加签名后的参数转JSON串
            String originalParamSignJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);

            /**加签后的参数AES加密************************************************************************/
            String aesKey = UUID.randomUUID().toString();//随机生成AES密码
            String encryptData = AESUtil.encrypt(originalParamSignJsonStr, aesKey);//加签后的参数AES加密

            /**随机AES密码进行RSA加密(钱包侧公钥加密)***********************************************************************************/
            String encryptKey = RSAUtils.publicEncrypt(aesKey, this.lzPublicKey);

            /**最后生成机密后的参数**************************************************************************************/
            WalletApiParamEncryptReq apiParamEncryptReq = new WalletApiParamEncryptReq();
            apiParamEncryptReq.setAppId(this.qdAppId);
            apiParamEncryptReq.setEncryptKey(encryptKey);
            apiParamEncryptReq.setEncryptData(encryptData);
            log.info("apiParamEncryptReq 生成最后参数= {}", JSONObject.toJSONString(apiParamEncryptReq));
            return apiParamEncryptReq;
        } catch (Exception e) {
            log.error("apiParamEncryptReq error ,req = {},errorMsg = {}", originalParamMap, e.getMessage());
        }
        return new WalletApiParamEncryptReq();
    }

    //解密钱包返回值
    private String walletResponseDecrypt(JSONObject jsonObject) {
        try {
            /**获取加密返回值encryptKey和encryptData*********************************************************************************/
            String encryptKey = jsonObject.getString("encryptKey");
            String encryptData = jsonObject.getString("encryptData");

            /**解密得到AES的key(收款模块侧私钥解密)************************************************************************************************************/
            String aesKey = RSAUtils.privateDecrypt(encryptKey, qdPrivateKey);

            /**aes的key解密加密回参*********************************************************************************************************************/
            return AESUtil.decrypt(encryptData, aesKey);
        } catch (Exception e) {
            log.error("walletResponseDecrypt error req = {},error = {}", JSONObject.toJSONString(jsonObject), e.getMessage());
            return "error";
        }
    }
}
