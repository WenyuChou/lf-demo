package com.example.demo.service;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;
import com.example.demo.util.OnlineSql;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenyu zhou
 * @version 2023-04-01
 */
public class GlzWash {


    public static String doWash(String oa, BigDecimal washAmt, String refundNo) {
        OnlineSql onlineSql = new OnlineSql();
        String sqlLMid = String.format("select * from t_account_desensitization where account_no = '%s';", oa);
        List<JSONObject> js = onlineSql.querySql(sqlLMid);
        if (js.size() != 1) {
            System.out.println(oa + "-- 龙民id没查到，暂停！！！");
            return oa + "-- 龙民id没查到，暂停！！！";
        }
        String longminId = js.get(0).getString("longmin_id");
        //查询管理者余额
        String sqlBalance = String.format("select * from lf_account where user_id = '%s' and account_type = 3 and `status` = 1;", longminId);
        List<JSONObject> balanceJs = onlineSql.querySql(sqlBalance);
        if (balanceJs.size() != 1) {
            System.out.println(oa + "-- 余额没查到，暂停！！！");
            return oa + "-- 余额没查到，暂停！！！";
        }
        BigDecimal balance = new BigDecimal(balanceJs.get(0).getString("balance"));
        if (balance.compareTo(BigDecimal.ZERO) < 1) {
            System.out.println(oa + "-- 这人没余额了，不清他了！！！余额：" + balance);
            return oa + "-- 这人没余额了，不清他了！！！管理者账户余额：" + balance;
        }
        BigDecimal realWash = washAmt.min(balance);
        String refundNoSql = "";
        if (StringUtils.isNotBlank(refundNo)) {
            refundNoSql = String.format("and request_no = '%s' ", refundNo);
        }
        //查询发放单，根据发放单撤回
        String sqlGrant = String.format("select * from t_grant_record where user_id = '%s' and account_type = 3 and oa = '%s' and business_type = '020' and `status` = 1 and create_time < '2023-01-01' and create_time > '2022-01-01' %s order by grant_amount desc;", longminId, oa, refundNoSql);
        List<JSONObject> grantJs = onlineSql.querySql(sqlGrant);
        if (grantJs.size() == 0) {
            System.out.println(oa + "-- 发放原单没查到，暂停！！！");
            return oa + "-- 发放原单没查到，暂停！！！";
        }
        List<JSONObject> batchWash = new ArrayList<>();
        for (JSONObject grantJ : grantJs) {
            String sqlRefund = String.format("select ifnull(sum(refunded_amount),0) as refundAmt from t_grant_refund_record where refund_no = '%s' and `status` = 1;", grantJ.getString("request_no"));
            List<JSONObject> refundJs = onlineSql.querySql(sqlRefund);
            BigDecimal refundAmt = refundJs.get(0).getBigDecimal("refundAmt");
            BigDecimal grant_amount = grantJ.getBigDecimal("grant_amount");
            if (refundAmt.compareTo(BigDecimal.ZERO) > 0) {
                grant_amount = grant_amount.subtract(refundAmt);
            }
            if(grant_amount.compareTo(BigDecimal.ZERO) < 1){
                continue;
            }
            BigDecimal singleWash = grant_amount.min(realWash);
            realWash = realWash.subtract(singleWash);
            grantJ.put("singleWash", singleWash);
            batchWash.add(grantJ);
            //realWash <= 0 时跳出，理论上只能 == 0
            if (BigDecimal.ZERO.compareTo(realWash) > -1) {
                break;
            }
        }
        //如果 != 0 分配有问题，或者单据总额不足以支撑撤回
        if (BigDecimal.ZERO.compareTo(realWash) != 0) {
            System.out.println(oa + "-- 单据金额不足以撤回，需手动操作！");
            return oa + "-- 单据金额不足以撤回，需手动操作！";
        }
        String[] washResult = doWash(batchWash);
        if (washResult == null) {
            return "";
        }
        String res = "[" + oa + "]" + "  \t" + washAmt + "  \t♥" + balance + "  \t" + washResult[0] + "  \t" + washResult[1];
        System.out.println(res);
        return res;
    }

    public static String[] doWash(List<JSONObject> batchWash) {
        StringBuilder req = new StringBuilder();
        BigDecimal sumWash = BigDecimal.ZERO;
        for (JSONObject wash : batchWash) {
            String reqNo = getReqNo();
            if (!backFFAccount(wash, reqNo, wash.getBigDecimal("singleWash"))) {
                return null;
            }
            if (!backQB8888(wash, reqNo, wash.getBigDecimal("singleWash"))) {
                return null;
            }
            sumWash = sumWash.add(wash.getBigDecimal("singleWash"));
            req.append(",").append(reqNo);
        }
        return new String[]{sumWash.toPlainString(), req.toString()};
    }

    /**
     * 发放账户撤回
     */
    public static boolean backFFAccount(JSONObject grantOrder, String reqNo, BigDecimal realWash) {
        String url = "https://api.longhu.net/longem-integrate-prod/api/v1/integrate/combine/refund";
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "64662ff0-122b-4e7e-a58b-a2c3e4bd6c80");
        heads.put("longfor-payment-service-token", "35edbf9ce7deaf8fedae6e4f39d40523e54219ce");
        JSONObject ffJs = new JSONObject();
        ffJs.put("refundType", 2);
        ffJs.put("orderNo", reqNo);
        ffJs.put("requestNo", reqNo);
        ffJs.put("refundAmtType", 2);
        ffJs.put("refundAccountType", 3);
        ffJs.put("refundNo", grantOrder.getString("request_no"));
        ffJs.put("amount", realWash);
        ffJs.put("allowDebit", false);
        ffJs.put("ffAccNo", grantOrder.getString("out_acc_no"));
        ffJs.put("source", grantOrder.getString("source"));
        ffJs.put("remark", "XM2124301-16679");
        String posts = HttpClient.doHttp(url, ffJs.toJSONString(), heads, "POST", 5)[1];
        JSONObject jsonObject = JSON.parseObject(posts);
        if ("0000".equals(jsonObject.getString("code")) && 1 == jsonObject.getJSONObject("data").getInteger("status")) {
            return true;
        }
        System.out.println("发放账户撤回错误！！！--- " + posts);
        return false;
    }

    public static boolean backQB8888(JSONObject grantJs, String reqNo, BigDecimal realWash) {
        String url = "https://api.longhu.net/longem-integrate-prod/api/v1/integrate/baccount/trade";
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "64662ff0-122b-4e7e-a58b-a2c3e4bd6c80");
        heads.put("longfor-payment-service-token", "35edbf9ce7deaf8fedae6e4f39d40523e54219ce");
        JSONObject qbJs = new JSONObject();
        qbJs.put("amount", realWash);
        qbJs.put("transType", "1");
        qbJs.put("businessType", "40212");
        qbJs.put("inAccNo", "QB-888888-888888");
        qbJs.put("inAccNoType", "25");
        qbJs.put("outAccNo", grantJs.getString("out_acc_no"));
        qbJs.put("outAccNoType", "20");
        qbJs.put("remark", "XM2124301-16679");
        qbJs.put("orderNo", getReqNo());
        qbJs.put("requestNo", reqNo);
        qbJs.put("appId", grantJs.getString("app_id"));
        qbJs.put("source", "-100");
        String posts = HttpClient.doHttp(url, qbJs.toJSONString(), heads, "POST", 5)[1];
        JSONObject jsonObject = JSON.parseObject(posts);
        if ("0000".equals(jsonObject.getString("code")) && jsonObject.getJSONObject("data").getString("requestNo").equals(reqNo)) {
            return true;
        }
        System.out.println("钱包总账户撤回错误！！！--- " + posts);
        return false;
    }
    public static boolean backQB8888X(JSONObject grantJs, String reqNo, BigDecimal realWash) {
        String url = "https://api.longhu.net/longem-integrate-prod/api/v1/integrate/baccount/trade";
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "64662ff0-122b-4e7e-a58b-a2c3e4bd6c80");
        heads.put("longfor-payment-service-token", "35edbf9ce7deaf8fedae6e4f39d40523e54219ce");
        JSONObject qbJs = new JSONObject();
        qbJs.put("amount", realWash);
        qbJs.put("transType", "2");
        qbJs.put("businessType", "40212");
        qbJs.put("inAccNo", "FF-");
        qbJs.put("inAccNoType", "20");
        qbJs.put("outAccNo", "用户longminid");
        qbJs.put("outAccNoType", "2");
        qbJs.put("remark", "XM2124301-16679");
        qbJs.put("orderNo", getReqNo());
        qbJs.put("requestNo", reqNo);
        qbJs.put("appId", grantJs.getString("app_id"));
        qbJs.put("source", "-100");
        String posts = HttpClient.doHttp(url, qbJs.toJSONString(), heads, "POST", 5)[1];
        JSONObject jsonObject = JSON.parseObject(posts);
        if ("0000".equals(jsonObject.getString("code")) && jsonObject.getJSONObject("data").getString("requestNo").equals(reqNo)) {
            return true;
        }
        System.out.println("钱包总账户撤回错误！！！--- " + posts);
        return false;
    }
    public static void main8(String[] args) {
         String s1 = GlzWash.doWash("chentao3", new BigDecimal("30897.9"), null);

        System.out.println("-------------------------------------------------------------------------");
        String sFin = "\n" + s1 ; /*+"\n" + s2 + "\n" + s3 + "\n" + s4 + "\n" + s5 + "\n" + s6 + "\n" + s7 + "\n" + s8 + "\n" + s9 + "\n" + s10 + "\n"
                + s11 + "\n" + s12 + "\n" + s13 + "\n" + s14 + "\n" + s15 + "\n" + s16 + "\n" + s17 + "\n" + s18 + "\n";// + s19 + "\n" + s20 + "\n";*/
        System.out.println(sFin);
    }

    private static final Snowflake snowflake = new Snowflake(1, 1);


    public static String getReqNo() {
        return "gw" + snowflake.nextId();
    }

}
