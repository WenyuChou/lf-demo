package com.example.demo.dds;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;
import com.example.demo.util.OnlineSql;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wenyu zhou
 * @version 2023-05-28
 */
@Slf4j
public class WashRefundBudgetMiss {

    public static void main(String[] args) {
        washRefundBudgetMiss(200983273);
    }

    public static void washRefundBudgetMiss(Integer refundOrderId) {
        OnlineSql onlineSql = new OnlineSql();
        String queryRefund = "select * from grant_refund_order where id = " + refundOrderId;
        JSONObject refundOrder = onlineSql.querySql(queryRefund).get(0);
        String queryOrigBudget = "select * from grant_budget_main where order_no = '"+refundOrder.getString("refund_no")+"' limit 1";
        JSONObject budgetGrant = onlineSql.querySql(queryOrigBudget).get(0);
        Long origId = budgetGrant.getLong("id");
        budgetGrant.remove("id");
        budgetGrant.put("request_no","refund"+refundOrder.getLong("id"));
        budgetGrant.put("order_no",refundOrder.getString("request_no"));
        budgetGrant.put("budget_occupy_amt",refundOrder.getBigDecimal("ac_refund_amount"));
        budgetGrant.remove("budget_batch_no");
        budgetGrant.remove("serial_no");
        budgetGrant.remove("budget_time");
        budgetGrant.put("status",0);
        budgetGrant.put("action_type",2);
        budgetGrant.remove("create_time");
        budgetGrant.remove("update_time");

        String queryOrigDetail = "select * from grant_budget_detail_lh where parent_id = " + origId + " limit 1";
        JSONObject budgetDetail = onlineSql.querySql(queryOrigDetail).get(0);
        budgetGrant.put("p_ysftbm",budgetDetail.getString("p_ysftbm"));
        budgetGrant.put("p_fx",budgetDetail.getString("p_fx"));
        budgetGrant.put("p_yslx",budgetDetail.getString("p_yslx"));
        budgetGrant.put("p_fylx",budgetDetail.getString("p_fylx"));
        budgetGrant.put("p_ftje",budgetDetail.getString("budget_occupy_amt"));

        System.out.println(JSON.toJSONString(budgetGrant));
        //
    }
    public static void doBudgetGrantSync(JSONObject jsonObject){
        String gateWay = "";
        String url = gateWay + "/grantBudget/syncBudgetMainInfo";
        Map<String,String> heads = new HashMap<>(8);
        heads.put("","");
        String result = HttpClient.doHttp(url,jsonObject.toJSONString(),heads,"POST",10)[1];
        System.out.println(result);
    }
}
