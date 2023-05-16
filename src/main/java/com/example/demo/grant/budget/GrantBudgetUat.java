package com.example.demo.grant.budget;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouwenyu
 * date 2023-03-03
 */
public class GrantBudgetUat {

    public static String ip = "http://10.31.99.46:9092";
    public static String resetBatchUrl = "/grantBudget/resetBudgetMainBatchNo";
    public static String updateDetailInfo = "/grantBudget/updateDetailInfo";

    public static void resetBatchNo(){
        String url = ip + resetBatchUrl;
        JSONArray arr = new JSONArray();
        arr.add("2023030118394245377160");
        String param = JSON.toJSONString(arr);
        Map<String,String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json");
        String post = HttpClient.doHttp(url, param, heads, "POST", 60)[1];
        System.out.println(post);
    }

    public static void updateDetailInfo(){
        String url = ip + updateDetailInfo;
        JSONObject js = new JSONObject();
        js.put("id",270838);
        js.put("pYsftbm","0010100410090020011");
        js.put("pYsftbmName","预算部门ABC");
        js.put("pFylx","09");
        String param = JSON.toJSONString(js);
        Map<String,String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json");
        String post = HttpClient.doHttp(url, param, heads, "POST", 60)[1];
        System.out.println(post);
    }

    public static void main(String[] args) {

    }
}
