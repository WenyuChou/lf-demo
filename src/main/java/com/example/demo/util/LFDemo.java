package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blinkfox.minitable.MiniTable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author zhouwenyu
 * date 2022-07-04
 */
@Slf4j
public class LFDemo {
    private String appId = "";
    //调用方公钥
    private String cPublicKey = "";
    //调用方私钥
    private String cPrivateKey = "";
    //平台公钥
    private String bPublicKey = "";
    //平台网关key
    private final String gaiaKey = "20cbdf74-2345-44e5-a374-6107c9b5eb27";


    public static void main(String[] args) {
        new LFDemo().issueBenefits();
    }

    /**
     * 业务调用
     */
    @SneakyThrows
    public void issueBenefits() {
        //调用url
        String url = "http://api.longfor.uat/XXX";
        Map<String, Object> paramMap = new HashMap<>();
        //业务参数，根据实际情况添加
        paramMap.put("activityType", "016");

        this.doHttp(url, paramMap);
    }


    @SneakyThrows
    public void doHttp(String url, Map<String, Object> paramMap) {
        WalletApiParamEncryptReq walletApiParamEncryptReq = this.apiParamEncryptReq(paramMap);
        log.info("⬇⬇⬇⬇⬇⬇开始调用入参数⬇⬇⬇⬇⬇⬇");
        log.info(JSON.toJSONString(walletApiParamEncryptReq));
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("X-Gaia-Api-Key", this.gaiaKey);
        head.put("X-longfor-app-id", this.appId);
        head.put("X-safety-source", "openApiSdk");
        String result = HttpClient.sendHttp(url, JSON.toJSONString(walletApiParamEncryptReq), head, "POST", 10)[1];
        log.info("⬇⬇⬇⬇⬇⬇调用完成返回⬇⬇⬇⬇⬇⬇");
        log.info(this.walletResponseDecrypt(JSONObject.parseObject(result)));
    }

    //调用龙湖（原始入参加签，加密）
    private WalletApiParamEncryptReq apiParamEncryptReq(Map<String, Object> originalParamMap) {
        originalParamMap.put("appId", this.appId);
        originalParamMap.put("timestamp", System.currentTimeMillis());
        originalParamMap.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        try {
            /**原始参数生成签名，并添加签名到原始参数*********************************************************/
            //原始参数转JSON字符串
            String originalParamJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);
            log.info("原始参数:originalParamJsonStr info = {}", originalParamJsonStr);
            //原始参数JSON串，私钥生成签名
            String sign = RSAUtils.sign(originalParamJsonStr, cPrivateKey);
            //生成的签名sign添加到原始参数中
            originalParamMap.put("sign", sign);
            //加签名后的参数转JSON串，按字母A~Z排序
            String originalParamSignJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);
            /**加签后的参数AES加密************************************************************************/
            String aesKey = UUID.randomUUID().toString();//随机生成AES密码
            String encryptData = AESUtil.encrypt(originalParamSignJsonStr, aesKey);//加签后的参数AES加密
            /**随机AES密码进行RSA加密(钱包侧公钥加密)***********************************************************************************/
            String encryptKey = RSAUtils.publicEncrypt(aesKey, bPublicKey);
            /**最后生成机密后的参数**************************************************************************************/
            WalletApiParamEncryptReq apiParamEncryptReq = new WalletApiParamEncryptReq();
            apiParamEncryptReq.setAppId(appId);
            apiParamEncryptReq.setEncryptKey(encryptKey);
            apiParamEncryptReq.setEncryptData(encryptData);
            log.info("apiParamEncryptReq 生成最后参数= {}", JSONObject.toJSONString(apiParamEncryptReq));
            return apiParamEncryptReq;
        } catch (Exception e) {
            log.error("apiParamEncryptReq error ,req = {},errorMsg = {}", originalParamMap, e.getMessage());
        }
        return new WalletApiParamEncryptReq();
    }

    //龙湖返回值
    private String walletResponseDecrypt(JSONObject jsonObject) {
        try {
            /**获取加密返回值encryptKey和encryptData*********************************************************************************/
            String encryptKey = jsonObject.getString("encryptKey");
            String encryptData = jsonObject.getString("encryptData");
            /**解密得到AES的key(收款模块侧私钥解密)************************************************************************************************************/
            String aesKey = RSAUtils.privateDecrypt(encryptKey, cPrivateKey);
            /**aes的key解密加密回参*********************************************************************************************************************/
            return AESUtil.decrypt(encryptData, aesKey);
        } catch (Exception e) {
            log.error("walletResponseDecrypt error req = {},error = {}", JSONObject.toJSONString(jsonObject), e.getMessage());
            return "error";
        }

    }
}
