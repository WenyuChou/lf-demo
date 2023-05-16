package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwenyu
 * date 2022-12-27
 */
public class GrantHzys {
    public static void main(String[] args) {
        OnlineSql onlineSql = new OnlineSql();
        String sql1 = "select distinct budget_batch_no  from grant_budget_main where budget_batch_no is not null and `status` = 0;";
        List<JSONObject> jsonObjects = onlineSql.querySql(sql1);
        List<String> cfBatchNo = new ArrayList<>();
        StringBuilder queryIn = new StringBuilder("(");
        for (int i = 0; i < jsonObjects.size(); i++) {
            String batchNo =  jsonObjects.get(i).getString("budget_batch_no");
            queryIn.append("'").append(batchNo).append("'");
            if(i!=jsonObjects.size()-1){
                queryIn.append(",");
            }
        }
        queryIn.append(")");
        String sql2 = "select distinct budget_batch_no from grant_budget_main where `status` = 1 and budget_batch_no in " + queryIn;
        List<JSONObject> jsonObjects1 = onlineSql.querySql(sql2);
        onlineSql.printResult(onlineSql.getColumns(), jsonObjects1);
    }
}
