package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.util.AESUtil;
import com.example.demo.util.HttpClient;
import com.example.demo.util.RSAUtils;
import com.example.demo.util.WalletApiParamEncryptReq;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RsaHttp {


    private static String appId = "009";
    private String bPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpOEb5vGe054rcK_aWfMGrfFS_YQ_ReKMtjdncv9NSWSor16NEyJSYV18BFtoTVg8ShL9K7u3k21fjVEHoTzu6Y4B35DBnuLkw7SvKcPodp0333GJu6r6TIw3hBMbw8z86cGo9OR9WIeNvjsZyRYMubKRHKJ6gdJGmLG2gV7iDwQIDAQAB";

    private static String cPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7MvCSkZT+zCk0327iDnqzXM6yPbDVhBk/pL1K4ITR17Q+EMSZkCGGxS8chqbdaOkSszFltauQjqFRrcO8fREAW6RRwA5+USjAcAp+ZEyN6TeE/kg2mlxQYz5z8nvPLBxAq0qdtP72qIgYTCjVUyJLXlSaeMLHUkQUv4W9B9SAZwIDAQAB";
    private static String cPriveteKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALsy8JKRlP7MKTTfbuIOerNczrI9sNWEGT+kvUrghNHXtD4QxJmQIYbFLxyGpt1o6RKzMWW1q5COoVGtw7x9EQBbpFHADn5RKMBwCn5kTI3pN4T+SDaaXFBjPnPye88sHECrSp20/vaoiBhMKNVTIkteVJp4wsdSRBS/hb0H1IBnAgMBAAECgYEAroT89we2KhzXxa0PyLvK9HBvWohc1ZHDoCvLNYU/DgrUxJAJWUifmO83zpZR9lSaBY79XF8TxT7TcjxXIOBP+bdFoXm7dZHf3rcLhskrHbBOgH+e0iLQRcbgkbI9oeyDLTq4a/jrFu7eBAuWN6nZSmWxqkFWFEP+e+8uKOK5QUECQQDcjPAcgjN6qoRrO6qc8uD2yT1IBNbpGYCmqeSNOTM6QAhoL+fwI6a0KeDBBKl0E6Hd/RorBUrIpCeCbrHupdMXAkEA2UmsxsPhMbpxL5hXYiVMD2IABU5V+SJlLi2IxZ/YGHzBkYrbPRaTf1RKjzIz3ZQSAycsju9do+r+r+l/dYVPMQJAC4r8ziEqXJMsFwiqZ0h26bOil3BZZx3Ek/R0V8fUnInZZsqLExBqmydN8gwyajbU0+95PSMrxNCCmNr+SQU4xQJAcFKLg/xB7wjn9aiUk2GgXdtVE2kZPD4xe5uCNlaYyJ/FGE1lY8DF4m5aVIepFAO1A/g+Km1GRLP3kEvt3XxaIQJAEqXU96PUV8KT9//9sif/jyZk8KP/wlDhTtMCkpDuxvxdoJ/nuA0YYpDTdKboxNWUIkh6IcjGhYmumHOcYpe/vw==";


    @SneakyThrows
    public static void main2(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        //paramMap.put("appId","014");
        //paramMap.put("longminId", "1031287");
        paramMap.put("timestamp", System.currentTimeMillis() + 60000);
        paramMap.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        paramMap.put("appId", "009");
        paramMap.put("companyName", "珠海市龙湖商业管理有限公司");
        paramMap.put("accName", "");
        paramMap.put("ncCode", "27025");
        paramMap.put("bizSysId", "002");
        paramMap.put("channel", "C2");
        paramMap.put("activityType", "001");
        paramMap.put("mgrOa", "shenguangliang");
        WalletApiParamEncryptReq walletApiParamEncryptReq = new RsaHttp().apiParamEncryptReq(paramMap);
        System.out.println(JSON.toJSONString(walletApiParamEncryptReq));
        //String url = "https://openapi.longhu.net//lf-wallet-prod";
        //String url = "http://10.231.132.218:8023/userAccount/queryGiveOutSum";
        //Map<String, String> head = new HashMap<>();
        //head.put("Content-Type", "application/json");
        //String result = HttpClient.sendHttp(url, JSON.toJSONString(walletApiParamEncryptReq), head, "POST", 10);
        //System.out.println(new RsaHttp().walletResponseDecrypt(JSONObject.parseObject(result)));
    }


    private void walletDecode() {
        //String aesKey = RSAUtils.privateDecrypt(walletApiParamEncryptReq.getEncryptKey(),walletPriKey);
        //String originalParam = AESUtil.decrypt(walletApiParamEncryptReq.getEncryptData(), aesKey);
        //JSONObject originalParamObj = JSONObject.parseObject(originalParam);
        //System.out.println(originalParamObj);
    }


    //调用钱包（原始入参加签，加密）
    private WalletApiParamEncryptReq apiParamEncryptReq(Map<String, Object> originalParamMap) {

        try {
            /**原始参数生成签名，并添加签名到原始参数*********************************************************/
            //原始参数转JSON字符串
            String originalParamJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);
            log.info("原始参数:originalParamJsonStr info = {}", originalParamJsonStr);

            //原始参数JSON串，私钥生成签名
            String sign = RSAUtils.sign(originalParamJsonStr, cPriveteKey);

            //生成的签名sign添加到原始参数中
            originalParamMap.put("sign", sign);

            //加签名后的参数转JSON串，按字母A~Z排序
            String originalParamSignJsonStr = JSON.toJSONString(originalParamMap, SerializerFeature.MapSortField);

            log.info("拼接sign后：{}", originalParamSignJsonStr);
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

    //解密钱包返回值
    private String walletResponseDecrypt(JSONObject jsonObject) {
        try {
            /**获取加密返回值encryptKey和encryptData*********************************************************************************/
            String encryptKey = jsonObject.getString("encryptKey");
            String encryptData = jsonObject.getString("encryptData");

            /**解密得到AES的key(收款模块侧私钥解密)************************************************************************************************************/
            String aesKey = RSAUtils.privateDecrypt(encryptKey, cPriveteKey);

            /**aes的key解密加密回参*********************************************************************************************************************/
            return AESUtil.decrypt(encryptData, aesKey);
        } catch (Exception e) {
            log.error("walletResponseDecrypt error req = {},error = {}", JSONObject.toJSONString(jsonObject), e.getMessage());
            return "error";
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCpOEb5vGe054rcK_aWfMGrfFS_YQ_ReKMtjdncv9NSWSor16NEyJSYV18BFtoTVg8ShL9K7u3k21fjVEHoTzu6Y4B35DBnuLkw7SvKcPodp0333GJu6r6TIw3hBMbw8z86cGo9OR9WIeNvjsZyRYMubKRHKJ6gdJGmLG2gV7iDwQIDAQAB";
        String pri = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKk4Rvm8Z7Tnitwr9pZ8wat8VL9hD9F4oy2N2dy_01JZKivXo0TIlJhXXwEW2hNWDxKEv0ru7eTbV-NUQehPO7pjgHfkMGe4uTDtK8pw-h2nTffcYm7qvpMjDeEExvDzPzpwaj05H1Yh42-OxnJFgy5spEconqB0kaYsbaBXuIPBAgMBAAECgYAfeP0SY60yI0JbUr3pHUjz-QrWEVpczkAjL9fOS8sk7LqALvoJx_iDIPTlgfeLUx04h4IAkNGkF_uPp1MevuXPplAP-A59twisMWDC2zR2b-nvTrX_XJokcC17-uAsLpdckLwIUsJTuN73kE12R0i1BtgbPyv9L6LR0xMkPrCGwQJBANYXv-4xT-96uVRTU4mMG1QXEQvzb6QhGhhtZ73-X0mkeDnIWix5ozZ9_FCrloQ6oLqF8Fwh67-zHiqOwVkgNGkCQQDKV-6AhQKKoJYXeIp6pp5S9GHFQPqkhyVx1cD-HIk0FXkkvyRp5-jps5zNDDUa7C2DeBS9bGEf0Ghr9ISaZYmZAkBIGoeReRoq1XJFbCreUEmD8KDVHY-j_2ICiBZAbsM40iOLkmaMRAsr9xJbOO7gab5oE2P4I5R6Y6DtRFbp5WbBAkBBGKDmP-RdbORckK8cXlY5Id4Cc7EvFTt6r-Xy2oNZej8LD2NLnwxTgzFC1laPXsQVs6AYgLGOZqHxeSzf7yKhAkEAz9qnew2-bS9SZWr_5RuQl1VJocvmQ3XQl0UvUteav-WCr9RKwAsJ25USpAWRFl4ZXMy2PJYo9MmCrAvP6SYFlQ";
        pri = pri.replace("\n", "");
        pub = pub.replace("\n","");
        System.out.println(pub);
        System.out.println(pri);
        String data = "hello";
        String sign = RSAUtils.sign(data, pri);
        System.out.println(RSAUtils.verify(data, pub, sign));
    }

    public static void main20(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        String data = "{\"requestNo\":\"92412bd79be7ec11b39ca60052ea850d202206091711171363\",\"activityType\":\"034\",\"appId\":\"017\",\"outAccNo\":\"FF-200602-00323\",\"batchNo\":\"92412bd79be7ec11b39ca60052ea850d202206091711172757\",\"grantAmount\":3.0,\"accountType\":2,\"userId\":\"60228404\",\"phoneNo\":\"15801076534\",\"oa\":\"\",\"extra\":\"\",\"nonce\":\"da2c26a790ce4a80b8bd8d1c23e1ceb3\",\"remark\":\"\",\"timestamp\":\"1654794677437\"}";
        String sign = RSAUtils.sign(data, cPriveteKey);
        System.out.println(sign);
    }

    @SneakyThrows
    public static void main1(String[] args) {
        //uat C1工单系统
        JSONObject jsonObject = JSON.parseObject("{\"batchNo\":\"P-C1-20220609002002\",\"activityType\":\"027\",\"appId\":\"009\",\"ncCode\":\"90001275\",\"rechargeType\":0,\"serialType\":2,\"serialNo\":\"BJWHTFK202206090001\",\"businessNum\":\"P-C1-20220609002002\",\"totalGrantAmount\":1000.0,\"grantCount\":1,\"timestamp\":\"1654796268831\",\"grantList\":[{\"grantAmount\":1000.0,\"requestNo\":\"P-C1-20220609002002|\",\"accountType\":2,\"userId\":\"52902180\",\"phoneNo\":\"15253020021\"}]}");
        Map<String, Object> paramMap = new HashMap<>(jsonObject);
        paramMap.put("timestamp", System.currentTimeMillis() + 60000);
        WalletApiParamEncryptReq walletApiParamEncryptReq = new RsaHttp().apiParamEncryptReq(paramMap);
        System.out.println(JSON.toJSONString(walletApiParamEncryptReq));
        String url = "https://api-uat.longfor.com/longem-gateway-uat/grant/batchAsync";
        //String url = "http://10.231.132.218:8023/userAccount/queryGiveOutSum";
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("X-safety-source", "openApiSdk");
        head.put("X-longfor-app-id", "009");
        head.put("X-Gaia-Api-Key", "332016b4-4d26-43e1-81f2-d90b0b062244");
        String[] result = HttpClient.sendHttp(url, JSON.toJSONString(walletApiParamEncryptReq), head, "POST", 10);
        System.out.println(new RsaHttp().walletResponseDecrypt(JSONObject.parseObject(result[1])));
    }

}
