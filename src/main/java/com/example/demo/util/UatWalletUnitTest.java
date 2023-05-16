package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.config.AppKeys;
import com.example.demo.service.RsaHttp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhouwenyu
 * date 2022-07-04
 */
@Slf4j
public class UatWalletUnitTest {
    private String appId = "";
    private String cPublicKey = "";
    private String cPrivateKey = "";
    private String bPublicKey = "";
    private String gaiaKey = "053ffea7-966a-412a-9b1b-11730574d2d5";

    {
        String appId = "014";
        this.appId = AppKeys.getAppInfo(appId).getAppId();
        this.cPublicKey = AppKeys.getAppInfo(appId).getCPublicKey();
        this.cPrivateKey = AppKeys.getAppInfo(appId).getCPrivateKey();
        this.bPublicKey = AppKeys.getAppInfo(appId).getBPublicKey();
    }


    public static void main(String[] args) {
        new UatWalletUnitTest().issueBenefits();
    }

    /**
     * 权益发放
     */
    @SneakyThrows
    public void issueBenefits() {
        String url = "http://api.longfor.uat/lf-wallet-trading-endpoint-uat/api/terrace/accountFf/issueBenefits/encrypt";
        Map<String, Object> paramMap = new HashMap<>();
        String transNo = createTransNo("LZUatTest");
        paramMap.put("timestamp", System.currentTimeMillis() + 60000);
        paramMap.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        paramMap.put("appId", this.appId);
        paramMap.put("activityType", "016");
        paramMap.put("ncCode", "1111");
        paramMap.put("batchNo", transNo);
        paramMap.put("mgrOA", "liubaini");
        paramMap.put("pBz", "CNY");
        paramMap.put("pFkdw", "00003");
        paramMap.put("pFkdwName", "A00003");
        paramMap.put("pLyxt", "LHLBXT");
        paramMap.put("pLyxtid", "LB201909260002");
        paramMap.put("pSkdw", "3619135676466176");
        paramMap.put("pSqr", "100114110");
        paramMap.put("pSsbm", "000");
        paramMap.put("pSsxm", "1000005313");
        paramMap.put("pZdr", "100114110");
        paramMap.put("transNo", transNo);
        paramMap.put("tradeSummary", "权益发放测试");
        List<Object> list = new ArrayList<>();
        Map<String, Object> childMap = new HashMap<>();
        childMap.put("pFx", "CA11");
        childMap.put("pFylx", "0004");
        childMap.put("pYsftbm", "001007003001");
        childMap.put("pYslx", "0001");
        childMap.put("pFtje", "10.0");
        list.add(childMap);
        paramMap.put("budgeDataList", list);
        paramMap.put("userId", "52652709");
        paramMap.put("rechargeAmt", "10.0");
        this.doHttp(url, paramMap);
    }


    public String createTransNo(String pre) {
        return pre + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
    }

    @SneakyThrows
    public void doHttp(String url, Map paramMap) {
        WalletApiParamEncryptReq walletApiParamEncryptReq = this.apiParamEncryptReq(paramMap);
        System.out.println("⬇⬇⬇⬇⬇⬇开始调用入参数⬇⬇⬇⬇⬇⬇");
        System.out.println(JSON.toJSONString(walletApiParamEncryptReq));
        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "application/json");
        head.put("X-Gaia-Api-Key", this.gaiaKey);
        String result = HttpClient.sendHttp(url, JSON.toJSONString(walletApiParamEncryptReq), head, "POST", 10)[1];
        System.out.println("⬇⬇⬇⬇⬇⬇调用完成返回⬇⬇⬇⬇⬇⬇");
        System.out.println(this.walletResponseDecrypt(JSONObject.parseObject(result)));
    }

    //调用钱包（原始入参加签，加密）
    private WalletApiParamEncryptReq apiParamEncryptReq(Map<String, Object> originalParamMap) {

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
            String aesKey = RSAUtils.privateDecrypt(encryptKey, cPrivateKey);

            /**aes的key解密加密回参*********************************************************************************************************************/
            return AESUtil.decrypt(encryptData, aesKey);
        } catch (Exception e) {
            log.error("walletResponseDecrypt error req = {},error = {}", JSONObject.toJSONString(jsonObject), e.getMessage());
            return "error";
        }

    }
}
