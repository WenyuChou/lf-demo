package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.GrantBudgetMain;

import java.util.*;

/**
 * @author wenyu zhou
 * @version 2023-05-16
 */
public class budgetSyncTest {
    public static void main(String[] args) {
        //syncBudgetMain();
        //deleteBudgetMain();
        //deleteBudgetDetailLh();
        //updateGrantOrder();
        updateGrantBatchInf();
    }

    public static String prod = "https://api.longhu.net/longem-migrate-prod";
    public static String uat = "http://10.31.99.46:9092";

    public static void syncBudgetMain() {
        OnlineSql onlineSql = new OnlineSql();
        String sql = "select * from t_grant_budget_prehandle where create_time > '2023-05-16 22:00:00' order by create_time desc limit 10";
        List<JSONObject> jsonObjects = onlineSql.querySql(sql);
        System.out.println(jsonObjects.get(0).toJSONString());
        GrantBudgetMain grantBudgetMain = changeBudgetMain(jsonObjects.get(0), onlineSql);
        System.out.println(JSON.toJSONString(grantBudgetMain));
        doSync(grantBudgetMain);
    }

    public static void doSync(GrantBudgetMain main) {
        String url = prod + "/grantBudget/syncBudgetMainInfo";
        String[] posts = HttpClient.doHttp(url, JSON.toJSONString(main), "POST", 10);
        System.out.println(posts[0] + " --- " + posts[1]);
    }


    public static void deleteBudgetMain() {
        List<Integer> ids = Arrays.asList(77749087, 77749086, 77749085, 77749084, 77749083, 77749082, 77749081, 77749080, 77749079, 77749078, 77749077, 77749076, 77749075, 77749074, 77749073, 77749072, 77749071, 77749070, 77749069, 77749068, 77749067, 77749066, 77749065, 77749064, 77749063);
        System.out.println(ids.size());
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "1de828d1-5105-410b-a03b-75fe1b7892f7");
        for (Integer id : ids) {
            JSONObject js = new JSONObject();
            js.put("id", Long.valueOf(id));
            String url = prod + "/grantBudget/cleanUpBudgetMainInfo";
            String[] posts = HttpClient.doHttp(url, js.toJSONString(), heads, "POST", 10);
            System.out.println(posts[0] + " --- " + posts[1]);
        }
    }

    public static void deleteBudgetDetailLh() {
        List<Integer> ids = Arrays.asList(77751866, 77751867, 77751868, 77751869, 77751870, 77751871, 77751872, 77751873, 77751874, 77751875, 77751876, 77751877, 77751878, 77751879, 77751880, 77751881, 77751882, 77751883, 77751884, 77751885, 77751886, 77751887, 77751888, 77751889, 77751890);
        System.out.println(ids.size());
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "1de828d1-5105-410b-a03b-75fe1b7892f7");
        for (Integer id : ids) {
            JSONObject js = new JSONObject();
            js.put("id", Long.valueOf(id));
            String url = prod + "/grantBudget/cleanUpGrantBudgetDetailLhInfo";
            String[] posts = HttpClient.doHttp(url, js.toJSONString(),heads, "POST", 10);
            System.out.println(posts[0] + " --- " + posts[1]);
        }
    }


    public static void updateGrantOrder() {
        List<Integer> ids = Arrays.asList(323844726, 323844729, 323844732, 323844737, 323844742, 323844748, 323844756, 323844760, 323844767, 323844776, 323844777, 323844780, 323844792, 323844811, 323844814, 323844824, 323844839, 323844850, 323844870, 323844877, 323844883, 323844894, 323844895, 323844903, 323844914);
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "1de828d1-5105-410b-a03b-75fe1b7892f7");
        for (Integer id : ids) {
            JSONObject js = new JSONObject();
            js.put("id", Long.valueOf(id));
            js.put("status", 0);
            js.put("errorMsg", " ");
            String url = prod + "/grantBudget/updateGrantOrderById";
            String[] posts = HttpClient.doHttp(url, js.toJSONString(),heads, "POST", 10);
            System.out.println(posts[0] + " --- " + posts[1]);
        }
    }

    public static void updateGrantBatchInf() {
        List<Integer> ids = Arrays.asList(323346408, 323346421, 323346455, 323346456, 323346471, 323346493, 323346529, 323346593, 323346416, 323346439, 323346459, 323346490, 323346549, 323346556, 323346574, 323346573, 323346405, 323346411, 323346427, 323346435, 323346446, 323346503, 323346518, 323346562, 323346582);
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "1de828d1-5105-410b-a03b-75fe1b7892f7");
        for (Integer id : ids) {
            JSONObject js = new JSONObject();
            js.put("id", Long.valueOf(id));
            js.put("status", 0);
            js.put("errorMsg", " ");
            String url = prod + "/grantBudget/updateGrantBatchInfById";
            String[] posts = HttpClient.doHttp(url, js.toJSONString(),heads, "POST", 10);
            System.out.println(posts[0] + " --- " + posts[1]);
        }

    }


    public static GrantBudgetMain changeBudgetMain(JSONObject json, OnlineSql onlineSql) {
        GrantBudgetMain main = new GrantBudgetMain();
        // main.setRequestNo();
        String sql = "select id from grant_order where request_no = '" + json.getString("trans_no") + "'";
        List<JSONObject> jsonObjects = onlineSql.querySql(sql);
        if (!jsonObjects.isEmpty()) {
            main.setRequestNo(jsonObjects.get(0).getLong("id") + "");
        }
        main.setSource("1.0");
        main.setOrderNo(json.getString("trans_no"));
        main.setBudgetOccupyAmt(json.getBigDecimal("budget_occupy_amt"));
        main.setSerialType(0);
        main.setStatus(json.getInteger("status"));
        main.setBudgetOccupyType(1);
        main.setGrantType(json.getInteger("grant_type"));
        main.setActionType(json.getInteger("trans_type"));
        main.setAppId(json.getString("app_id"));
        main.setPSsxm(json.getString("p_ssxm"));
        main.setPFkdw(json.getString("p_fkdw"));
        main.setPFkdwName(json.getString("p_fkdw_name"));
        main.setPBz(json.getString("p_bz"));
        main.setPZdr(json.getString("p_zdr"));
        main.setPSqr(json.getString("p_sqr"));
        main.setPSsbm(json.getString("p_ssbm"));
        main.setPSkdw(json.getString("p_skdw"));
        main.setPLyxtid(json.getString("p_lyxtid"));
        main.setPLyxt(json.getString("p_lyxt"));
        main.setIsDeleted(0);
        main.setSeq(0L);
        return main;
    }
}
