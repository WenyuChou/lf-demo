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
        budgetGrant.put("p_ftje",budgetGrant.getBigDecimal("budget_occupy_amt"));

        System.out.println(JSON.toJSONString(budgetGrant));
        JSONObject param = packageParamMain(budgetGrant);
        doBudgetGrantSync(param);
    }
    public static JSONObject packageParamMain(JSONObject jsonObject){
        JSONObject param = new JSONObject();
        param.put("requestNo",jsonObject.get("request_no"));
        param.put("source",jsonObject.get("source"));
        param.put("orderNo",jsonObject.get("order_no"));
        param.put("budgetOccupyAmt",jsonObject.get("budget_occupy_amt"));
        param.put("serialType",jsonObject.get("serial_type"));
        param.put("status",jsonObject.get("status"));
        param.put("budgetOccupyType",jsonObject.get("budget_occupy_type"));
        param.put("grantType",jsonObject.get("grant_type"));
        param.put("actionType",jsonObject.get("action_type"));
        param.put("appId",jsonObject.get("app_id"));
        param.put("pSsxm",jsonObject.get("p_ssxm"));
        param.put("pFkdw",jsonObject.get("p_fkdw"));
        param.put("pFkdwName",jsonObject.get("p_fkdw_name"));
        param.put("pBz",jsonObject.get("p_bz"));
        param.put("pZdr",jsonObject.get("p_zdr"));
        param.put("pSqr",jsonObject.get("p_sqr"));
        param.put("pSsbm",jsonObject.get("p_ssbm"));
        param.put("pSkdw",jsonObject.get("p_skdw"));
        param.put("pLyxtid",jsonObject.get("p_lyxtid"));
        param.put("pLyxt",jsonObject.get("p_lyxt"));
        param.put("isDeleted",jsonObject.get("is_deleted"));
        param.put("seq",jsonObject.get("seq"));
        param.put("pYsftbm",jsonObject.get("p_ysftbm"));
        param.put("pFx",jsonObject.get("p_fx"));
        param.put("pYslx",jsonObject.get("p_yslx"));
        param.put("pFylx",jsonObject.get("p_fylx"));
        param.put("pFtje",jsonObject.get("p_ftje"));
        return param;
    }
    public static void doBudgetGrantSync(JSONObject jsonObject){
        String gateWay = "http://10.31.99.46:9092";
        //String gateWay = "https://api.longfor.com/longem-migrate-prod";
        String url = gateWay + "/grantBudget/syncBudgetMainInfo";
        Map<String,String> heads = new HashMap<>(8);
        heads.put("x-gaia-api-key","1de828d1-5105-410b-a03b-75fe1b7892f7");
        String result = HttpClient.doHttp(url,jsonObject.toJSONString(),heads,"POST",10)[1];
        System.out.println(result);
    }
}
