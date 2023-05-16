package com.example.demo.dds;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;
import com.example.demo.util.OnlineSql;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.util.FileUtil.readJson;
import static com.example.demo.util.FileUtil.writeJson;

/**
 * @author zhouwenyu
 * date 2022-11-04
 */
@Slf4j
public class WashBudgetTrans1 {

    public static void main(String[] args) throws Exception {
        wash();
    }

    @SneakyThrows
    public static void wash() {
        OnlineSql sql = new OnlineSql();
        JSONObject jsonData = readJson();
        int maxId = jsonData.getInteger("maxId");
        String maxIdSql = "and t1.id > "+ maxId + " ";
        String sql1 = "select concat(\"{\\\"optType\\\":\\\"7\\\",\\\"data\\\":{\\\"id\\\":\\\"\",t1.id,\"\\\",\\\"pYsftbm\\\":\\\"\",\"001004013001002001002\",\"\\\"}}\") washVal from t_grant_budget_prehandle t left join t_grant_budget_expense t1 on t.id = t1.budget_prehandle_id where t.status = 0 and t1.p_ysftbm = '001004005001002002' "+ maxIdSql +" order by t1.id asc;";
        List<JSONObject> resultLog = sql.querySql(sql1);
        maxId = resultLog.get(resultLog.size()-1).getJSONObject("washVal").getJSONObject("data").getInteger("id");
        System.out.println(maxId);
        jsonData.put("maxId",maxId);
        writeJson(jsonData);
        int sum = 0;
        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json");
        heads.put("x-gaia-api-key", "1de828d1-5105-410b-a03b-75fe1b7892f7");
        HttpClient.print = false;
        while (resultLog.size() != 0) {

            for (JSONObject jsonObject : resultLog) {
                String washVal = jsonObject.getString("washVal");
                HttpClient.doHttp("https://api.longhu.net/longem-migrate-prod/api/migrate/grant/update/info", washVal, heads, "POST", 5);
            }
            Thread.sleep(Long.parseLong("3000"));
            sum = sum + resultLog.size();
            log.info("清洗完成个数：" + sum);
            jsonData = readJson();
            maxId = jsonData.getInteger("maxId");
            maxIdSql = "and t1.id > "+ maxId + " ";
            sql1 = "select concat(\"{\\\"optType\\\":\\\"7\\\",\\\"data\\\":{\\\"id\\\":\\\"\",t1.id,\"\\\",\\\"pYsftbm\\\":\\\"\",\"001004013001002001002\",\"\\\"}}\") washVal from t_grant_budget_prehandle t left join t_grant_budget_expense t1 on t.id = t1.budget_prehandle_id where t.status = 0 and t1.p_ysftbm = '001004005001002002' "+ maxIdSql +" order by t1.id asc;";
            resultLog = sql.querySql(sql1);
            if(resultLog.size()>0){
                maxId = resultLog.get(resultLog.size()-1).getJSONObject("washVal").getJSONObject("data").getInteger("id");
                System.out.println(maxId);
                jsonData.put("maxId",maxId);
                writeJson(jsonData);
            }
        }
    }
}
