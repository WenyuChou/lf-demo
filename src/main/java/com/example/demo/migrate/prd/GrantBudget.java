package com.example.demo.migrate.prd;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;
import com.example.demo.util.OnlineSql;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouwenyu
 * date 2023-03-30
 */
@Slf4j
public class GrantBudget {
    public static void main(String[] args) {

    }
    public static void updateDetailInfo() {
        String url = "https://api.longfor.com/longem-migrate-prod/grantBudget/updateDetailInfo";
        Map<String,String> head = new HashMap<>();
        head.put("x-gaia-api-key","1de828d1-5105-410b-a03b-75fe1b7892f7");
        OnlineSql onlineSql = new OnlineSql();
        String sql1 = "select d.id from grant_budget_detail_lh d\n" +
                "inner join grant_budget_main m on m.id = d.parent_id\n" +
                "where m.`status`=0 and  d.p_fylx is null";
        List<JSONObject> json = onlineSql.querySql(sql1);
        for (JSONObject jsonObject : json) {
            Long id = jsonObject.getLong("id");
            JSONObject param = new JSONObject();
            param.put("id",id);
            param.put("pFylx","04");
            log.info("param:{}",param.toJSONString());
            HttpClient.doHttp(url,param.toJSONString(),head,"POST",5);
        }
    }
}
