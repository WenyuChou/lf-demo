package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouwenyu
 * date 2021-12-06
 */
public class SingleGrantTest {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            String result = singleGrant("WALLET","10468696");
            System.out.println(i+result);
        }
    }

    public static String singleGrant(String type,String userId) {
        JSONObject js = new JSONObject();
        js.put("accountNo", "FF-210305-05605");
        js.put("activityType", "049");
        js.put("appId", "117");
        js.put("operator", "shenguangliang");
        js.put("rechargeAmt", "0.1");
        js.put("remark", "system_wash");
        js.put("sysType", type);
        js.put("extra","{\"activity_no\":\"YH2112301207CJ91401\",\"source\":\"platform_grant\"}");
        js.put("transNum", "transNo" + System.currentTimeMillis() + (int) (Math.random() * 1000));
        js.put("userId",userId);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Gaia-Api-Key","e182c51a-e259-43cb-88f0-526a3fcddb6d");
        headers.put("unencrypted-password","1qaz2wsx3edc");
        headers.put("Content-Type","application/json");
        String[] posts = HttpClient.doHttp("https://api-pre.longhu.net/lf-wallet-trading-endpoint-pre/api/terrace/singleIssue/noEncrypt", js.toJSONString(), headers, "POST", 10);
        return posts[1];
    }

}
