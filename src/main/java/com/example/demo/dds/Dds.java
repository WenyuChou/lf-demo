package com.example.demo.dds;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.OnlineSql;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouwenyu
 * date 2022-10-17
 */
public class Dds {

    public static void main(String[] args) {
        xfCalc();
    }

    public static void calcFk1() {
        OnlineSql sql = new OnlineSql();
        String sql1 = "select " +
                "*" +
                "from t_activity_result_log where serial_number = 'LB20221015073'";
        String sql2 = "select * from t_grant_budget_prehandle where p_fkpkey = 'LB20221015073'";
        List<JSONObject> resultLog = sql.querySql(sql1);
        List<JSONObject> prehand = sql.querySql(sql2);
        for (JSONObject jsonObject : prehand) {
            String trans_no = jsonObject.getString("trans_no");
            BigDecimal budget_occupy_amt = jsonObject.getBigDecimal("budget_occupy_amt");
            List<JSONObject> trans_no1 = resultLog.stream().filter(jsonObject1 -> jsonObject1.getString("trans_no").equals(trans_no)).collect(Collectors.toList());
            if (trans_no1.isEmpty()) {
                System.out.println("没找到---" + trans_no + "---" + budget_occupy_amt);
                continue;
            }
            if (trans_no1.size() != 1) {
                System.out.println("查询多条---" + trans_no);
                continue;
            }
            if (trans_no1.get(0).getBigDecimal("trans_amt").compareTo(budget_occupy_amt) != 0) {
                System.out.println("金额不一致---" + trans_no + "---trans_amt：" + trans_no1.get(0).getBigDecimal("trans_amt") + "---budget_occupy_amt：" + budget_occupy_amt);
            }
        }

    }

    //prehand 与 resultlog费控同步查询
    public static void calcFk() {

        OnlineSql sql = new OnlineSql();
        String sql1 = "SELECT p_fkpkey,IFNULL(sum(if(trans_type=2,-budget_occupy_amt,budget_occupy_amt)),0.0) grantNotApply\n" +
                "\t\tFROM t_grant_budget_prehandle\n" +
                "\t\tWHERE modify_time >= '2022-10-15 03:21:58' and modify_time <= '2022-10-15 03:51:58'\n" +
                "\t\tgroup by p_fkpkey";
        List<JSONObject> result = sql.querySql(sql1);
        sql.printResult(sql.getColumns(), result);
        for (JSONObject jsonObject : result) {
            String fk = jsonObject.getString("p_fkpkey");
            BigDecimal amt = jsonObject.getBigDecimal("grantNotApply");
            String sql2 = String.format("select IFNULL(sum(if(trans_type=1,-trans_amt,trans_amt)),0.0) as sumAmt from t_activity_result_log where serial_number = '%s';", fk);
            List<JSONObject> sql2List = sql.querySql(sql2);
            BigDecimal sumAmt = sql2List.get(0).getBigDecimal("sumAmt");
            if (amt.compareTo(sumAmt) != 0) {
                System.out.println("费控单号：" + fk + " 有误差");
            }
        }
    }

    //以商户纬度计算消费等式，发放 = 商户表 + 用户表

    /**
     select t.order_no,t.amt amt1,t1.out_trans_no,t1.amt as amt2 from(
     select
     IFNULL(sum(if(action_type in (1,4,5,6,8),trans_amount,-trans_amount)),0) amt,order_no
     from lf_account_log
     where other_acc_no like 'SH%'
     and create_time >='2022-11-14 19:01:20' and create_time <= '2022-11-15'
     group by order_no
     ) t left join
     (
     SELECT
     SUM(IF(trans_type = 1, trans_amt, -trans_amt)) amt,out_trans_no
     FROM t_merchant_transaction
     WHERE `status` in (1)
     and trans_type in (1, 2) and create_time >='2022-11-14 19:01:20' and create_time <= '2022-11-15'
     and acc_no like 'SH%' and (target_acc_no like 'XF%' or target_acc_no like 'YH%')
     group by out_trans_no
     ) t1 on t.order_no = t1.out_trans_no
     where t.amt != -t1.amt
     -- having amt1 is null;


    排查sql2：发放流水多，商户流水少
     select * from t_merchant_transaction where create_time >='2022-12-06 08:11:28' and create_time <= '2022-12-06 08:31:35' and acc_no =
     'SH-201222-000405' and target_acc_no like 'FF%' and out_trans_no not in(

     select order_no from lfm_account_log where create_time >='2022-12-06 08:11:28' and create_time <= '2022-12-06 08:31:35'
     and account_type=20 and other_account_type!=25 and other_account = 'SH-201222-000405'

     )


     排查sql3 退款情况，lf_account_log有， lfm没有
     select l.* ,m.id from
     (select * from lf_account_log where account_type in(1,2,3) and create_time >= '2023-02-18 11:43:36' and create_time <= '2023-02-19 19:09:08' and account_type in(1,2,3) and other_account_type = 20) l
     left join
     (select * from lfm_account_log where create_time >= '2023-02-18 11:43:36' and create_time <= '2023-02-19 19:09:08' and account_type = 20 and other_account_type in(1,2,3)) m
     on l.order_no = m.order_no
     where m.id is null;

     排查sql4 查询B端情况
     select * from t_btrade_log where status = 0 and seq = 0 and create_time >= '2023-02-17 11:43:36' and acc_no not like 'JS%' limit 10000;


     */
    public static void xfCalc() {
        OnlineSql sql = new OnlineSql();
        String time = "create_time >='2023-04-18 16:40:29' and create_time <= '2023-04-18 16:50:29'";
        String sql1 = String.format(
                "select distinct other_acc_no acc_no from lf_account_log where %s and other_account_type=22 " +
                        "union all " +
                        "select distinct acc_no from t_merchant_transaction where %s " +
                        "and `status` = 1 and target_acc_no not like 'FF%s'", time, time, "%");
        //所有发生动账的商户列表
        List<String> shList = sql.querySql(sql1).stream().distinct().map(js -> js.getString("acc_no")).collect(Collectors.toList());

        for (String sh : shList) {
            //查询发放
            String sql2 = String.format("SELECT \n" +
                    "IFNULL(sum(if(action_type in (1,3,4,6,8) ,-trans_amount,trans_amount)),0) amt\n" +
                    "from lfm_account_log t\n" +
                    "        where t.account_type=20 and t.other_account_type!=25\n" +
                    "        and %s\n" +
                    "        and other_account = '%s'", time, sh);
            BigDecimal amt1 = sql.querySql(sql2).get(0).getBigDecimal("amt");

            String sql3 = String.format("SELECT IFNULL(SUM(IF(trans_type = 1, trans_amt, -trans_amt)),0) amt\n" +
                    "FROM t_merchant_transaction \n" +
                    "WHERE `status` = 1\n" +
                    "and trans_type in (1, 2) and %s\n" +
                    "and acc_no = '%s' and target_acc_no not like 'SH%s' ", time, sh, "%");
            BigDecimal amt2 = sql.querySql(sql3).get(0).getBigDecimal("amt");

            String sql4 = String.format("select IFNULL(sum(if(action_type in (1,4,5,6,8),trans_amount,-trans_amount)),0) amt \n" +
                    "from lf_account_log\n" +
                    "where other_acc_no='%s'\n" +
                    "and %s", sh, time);
            BigDecimal amt3 = sql.querySql(sql4).get(0).getBigDecimal("amt");
            BigDecimal subtract = amt1.subtract(amt2).subtract(amt3);
            if (subtract.compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(sh + "----" + amt1 + "----" + amt2 + "----" + amt3 + "----sub:" + subtract);
            }
        }
    }
}
