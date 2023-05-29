package com.example.demo.dds;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.HttpClient;
import com.example.demo.util.MiniTableUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhouwenyu
 * date 2023-04-24
 * 发放账户 -> QB888
 */
public class DdsBackAmt {

    @Data
    public class backQBParam {
        private String ffAccount;
        private String appId;
        private String reqNo;
        private BigDecimal washAmt;

        backQBParam(String ffAccount, String appId, String reqNo, BigDecimal washAmt) {
            this.ffAccount = ffAccount;
            this.appId = appId;
            this.reqNo = reqNo;
            this.washAmt = washAmt;
        }
    }

    public static boolean backQB8888(String ffAccount, String appId, String reqNo, BigDecimal realWash) {
        String url = "https://api.longhu.net/longem-integrate-prod/api/v1/integrate/baccount/trade";
        Map<String, String> heads = new HashMap<>();
        heads.put("x-gaia-api-key", "64662ff0-122b-4e7e-a58b-a2c3e4bd6c80");
        heads.put("longfor-payment-service-token", "35edbf9ce7deaf8fedae6e4f39d40523e54219ce");
        JSONObject qbJs = new JSONObject();
        qbJs.put("amount", realWash);
        qbJs.put("transType", "1");
        qbJs.put("businessType", "001");
        qbJs.put("inAccNo", "QB-888888-888888");
        qbJs.put("inAccNoType", "25");
        qbJs.put("outAccNo", ffAccount);
        qbJs.put("outAccNoType", "20");
        qbJs.put("remark", "XM2248301-17472");
        qbJs.put("orderNo", getReqNo());
        qbJs.put("requestNo", reqNo);
        qbJs.put("appId", appId);
        qbJs.put("source", "-100");
        String posts = HttpClient.doHttp(url, qbJs.toJSONString(), heads, "POST", 5)[1];
        JSONObject jsonObject = JSON.parseObject(posts);
        if ("0000".equals(jsonObject.getString("code")) && jsonObject.getJSONObject("data").getString("requestNo").equals(reqNo)) {
            return true;
        }
        System.out.println("钱包总账户撤回错误！！！--- " + posts);
        return false;
    }

    private static final Snowflake snowflake = new Snowflake(1, 1);


    public static String getReqNo() {
        return String.valueOf(snowflake.nextId());
    }

    public void printTable() {
        List<backQBParam> list = buildParam();
        List<String> headers = Arrays.asList("ffAccount", "appId", "reqNo", "backAmt");
        List<List<Object>> lists = new ArrayList<>();
        for (backQBParam backQBParam : list) {
            List<Object> objectList = new ArrayList<>();
            objectList.add(backQBParam.getFfAccount());
            objectList.add(backQBParam.getAppId());
            objectList.add(backQBParam.getReqNo());
            objectList.add(backQBParam.getWashAmt());
            lists.add(objectList);
        }
        System.out.println(MiniTableUtil.print(headers, lists));
    }

    public List<backQBParam> buildParam() {
        List<backQBParam> list = new ArrayList<>();
        list.add(new backQBParam("FF-210818-11260", "003", getReqNo(), new BigDecimal("5500")));
        list.add(new backQBParam("FF-210416-06506", "003", getReqNo(), new BigDecimal("130")));
        list.add(new backQBParam("FF-220930-44666", "003", getReqNo(), new BigDecimal("11000")));
        list.add(new backQBParam("FF-200923-02058", "003", getReqNo(), new BigDecimal("13200")));
        list.add(new backQBParam("FF-210917-28203", "133", getReqNo(), new BigDecimal("50")));

        list.add(new backQBParam("FF-210917-28205", "113", getReqNo(), new BigDecimal("486.8")));
        list.add(new backQBParam("FF-210917-28281", "113", getReqNo(), new BigDecimal("468.1")));
        list.add(new backQBParam("FF-210917-28284", "113", getReqNo(), new BigDecimal("455.3")));
        list.add(new backQBParam("FF-210917-28352", "113", getReqNo(), new BigDecimal("227.1")));
        list.add(new backQBParam("FF-220709-44149", "113", getReqNo(), new BigDecimal("210.2")));
        list.add(new backQBParam("FF-220113-40986", "113", getReqNo(), new BigDecimal("202.6")));
        list.add(new backQBParam("FF-210917-28265", "113", getReqNo(), new BigDecimal("202.1")));
        list.add(new backQBParam("FF-210917-28201", "113", getReqNo(), new BigDecimal("195.8")));
        list.add(new backQBParam("FF-210917-28180", "113", getReqNo(), new BigDecimal("181")));
        list.add(new backQBParam("FF-210917-28278", "113", getReqNo(), new BigDecimal("156.5")));
        list.add(new backQBParam("FF-210917-28197", "113", getReqNo(), new BigDecimal("142.9")));
        list.add(new backQBParam("FF-210917-28253", "113", getReqNo(), new BigDecimal("131.6")));
        list.add(new backQBParam("FF-210830-12020", "113", getReqNo(), new BigDecimal("131.3")));
        list.add(new backQBParam("FF-210917-28286", "113", getReqNo(), new BigDecimal("128.4")));
        list.add(new backQBParam("FF-210917-28264", "113", getReqNo(), new BigDecimal("128.4")));
        list.add(new backQBParam("FF-210917-28255", "113", getReqNo(), new BigDecimal("118.5")));
        list.add(new backQBParam("FF-210917-28251", "113", getReqNo(), new BigDecimal("100")));
        list.add(new backQBParam("FF-210917-28285", "113", getReqNo(), new BigDecimal("97.5")));
        list.add(new backQBParam("FF-210917-28283", "113", getReqNo(), new BigDecimal("97.4")));
        list.add(new backQBParam("FF-210917-28303", "113", getReqNo(), new BigDecimal("88.5")));
        list.add(new backQBParam("FF-210917-28273", "113", getReqNo(), new BigDecimal("83.4")));
        list.add(new backQBParam("FF-210917-28252", "113", getReqNo(), new BigDecimal("81.9")));
        list.add(new backQBParam("FF-210913-27710", "113", getReqNo(), new BigDecimal("80.7")));
        list.add(new backQBParam("FF-210917-28269", "113", getReqNo(), new BigDecimal("75.1")));
        list.add(new backQBParam("FF-210917-28258", "113", getReqNo(), new BigDecimal("67.6")));
        list.add(new backQBParam("FF-210917-28272", "113", getReqNo(), new BigDecimal("63.4")));
        list.add(new backQBParam("FF-210917-28260", "113", getReqNo(), new BigDecimal("60.6")));
        list.add(new backQBParam("FF-220910-44534", "113", getReqNo(), new BigDecimal("55.7")));
        list.add(new backQBParam("FF-220307-42719", "113", getReqNo(), new BigDecimal("53.2")));
        list.add(new backQBParam("FF-210917-28259", "113", getReqNo(), new BigDecimal("53")));
        list.add(new backQBParam("FF-211104-39409", "113", getReqNo(), new BigDecimal("47")));
        list.add(new backQBParam("FF-210917-28181", "113", getReqNo(), new BigDecimal("47")));
        list.add(new backQBParam("FF-210917-28302", "113", getReqNo(), new BigDecimal("42.8")));
        list.add(new backQBParam("FF-210917-28402", "113", getReqNo(), new BigDecimal("40.7")));
        list.add(new backQBParam("FF-210917-28276", "113", getReqNo(), new BigDecimal("34.5")));
        list.add(new backQBParam("FF-210917-28280", "113", getReqNo(), new BigDecimal("29.8")));
        list.add(new backQBParam("FF-210917-28291", "113", getReqNo(), new BigDecimal("28.8")));
        list.add(new backQBParam("FF-210917-28263", "113", getReqNo(), new BigDecimal("24.8")));
        list.add(new backQBParam("FF-221228-45147", "113", getReqNo(), new BigDecimal("23.5")));
        list.add(new backQBParam("FF-211001-29748", "113", getReqNo(), new BigDecimal("22.1")));
        list.add(new backQBParam("FF-211001-29748", "113", getReqNo(), new BigDecimal("22.1")));
        list.add(new backQBParam("FF-210917-28266", "113", getReqNo(), new BigDecimal("20.9")));
        list.add(new backQBParam("FF-210917-28271", "113", getReqNo(), new BigDecimal("20.7")));
        list.add(new backQBParam("FF-220927-44625", "113", getReqNo(), new BigDecimal("20.4")));
        list.add(new backQBParam("FF-210824-11754", "113", getReqNo(), new BigDecimal("14.7")));
        list.add(new backQBParam("FF-210806-11047", "113", getReqNo(), new BigDecimal("13.3")));
        list.add(new backQBParam("FF-210917-28277", "113", getReqNo(), new BigDecimal("12.3")));
        list.add(new backQBParam("FF-221221-45099", "113", getReqNo(), new BigDecimal("11")));
        list.add(new backQBParam("FF-210824-11797", "113", getReqNo(), new BigDecimal("9.6")));
        list.add(new backQBParam("FF-210825-11885", "113", getReqNo(), new BigDecimal("9.2")));
        list.add(new backQBParam("FF-211204-40213", "113", getReqNo(), new BigDecimal("8.5")));
        list.add(new backQBParam("FF-221216-45075", "113", getReqNo(), new BigDecimal("8.3")));
        list.add(new backQBParam("FF-210824-11769", "113", getReqNo(), new BigDecimal("8.2")));
        list.add(new backQBParam("FF-210917-28274", "113", getReqNo(), new BigDecimal("8")));
        list.add(new backQBParam("FF-210824-11751", "113", getReqNo(), new BigDecimal("6.9")));
        list.add(new backQBParam("FF-211102-39378", "113", getReqNo(), new BigDecimal("6.9")));
        list.add(new backQBParam("FF-210917-28282", "113", getReqNo(), new BigDecimal("6.2")));
        list.add(new backQBParam("FF-210917-28240", "113", getReqNo(), new BigDecimal("5.8")));
        list.add(new backQBParam("FF-210825-11940", "113", getReqNo(), new BigDecimal("5.6")));
        list.add(new backQBParam("FF-210815-11212", "113", getReqNo(), new BigDecimal("5.1")));
        list.add(new backQBParam("FF-220406-43297", "113", getReqNo(), new BigDecimal("5")));
        list.add(new backQBParam("FF-220124-41267", "113", getReqNo(), new BigDecimal("4.8")));
        list.add(new backQBParam("FF-210917-28270", "113", getReqNo(), new BigDecimal("4.7")));
        list.add(new backQBParam("FF-210907-12785", "113", getReqNo(), new BigDecimal("4.5")));
        list.add(new backQBParam("FF-210917-28261", "113", getReqNo(), new BigDecimal("4.5")));
        list.add(new backQBParam("FF-211124-39689", "113", getReqNo(), new BigDecimal("3.9")));
        list.add(new backQBParam("FF-210917-28279", "113", getReqNo(), new BigDecimal("3.8")));
        list.add(new backQBParam("FF-210917-28295", "113", getReqNo(), new BigDecimal("3.7")));
        list.add(new backQBParam("FF-210917-28200", "113", getReqNo(), new BigDecimal("3.1")));
        list.add(new backQBParam("FF-210825-11897", "113", getReqNo(), new BigDecimal("2.6")));
        list.add(new backQBParam("FF-210824-11780", "113", getReqNo(), new BigDecimal("2.5")));
        list.add(new backQBParam("FF-210918-28468", "113", getReqNo(), new BigDecimal("2.3")));
        list.add(new backQBParam("FF-210917-28275", "113", getReqNo(), new BigDecimal("2.2")));
        list.add(new backQBParam("FF-210917-28216", "113", getReqNo(), new BigDecimal("2")));
        list.add(new backQBParam("FF-210909-27118", "113", getReqNo(), new BigDecimal("1.6")));
        list.add(new backQBParam("FF-210917-28268", "113", getReqNo(), new BigDecimal("1.1")));
        list.add(new backQBParam("FF-210824-11793", "113", getReqNo(), new BigDecimal("1.1")));
        list.add(new backQBParam("FF-220210-42412", "113", getReqNo(), new BigDecimal("0.8")));
        list.add(new backQBParam("FF-220121-41202", "113", getReqNo(), new BigDecimal("0.8")));
        list.add(new backQBParam("FF-210917-28307", "113", getReqNo(), new BigDecimal("0.4")));
        list.add(new backQBParam("FF-220403-43289", "113", getReqNo(), new BigDecimal("0.2")));

//11067.2
        return list;
    }
/*
        142.3 - (88.5+0.4+8.7)
select * from lfm_account_log where account_type = 20 and
order_no in('QYGB422270902397272078','365897369281953863','QYGB422397346234466366')
 */

    public static void main(String[] args) {
        DdsBackAmt dds = new DdsBackAmt();
        List<backQBParam> list = dds.buildParam();
        Optional<BigDecimal> reduce = list.stream().map(backQBParam::getWashAmt).reduce(BigDecimal::add);
        dds.printTable();
        BigDecimal bigDecimal = reduce.get();
        System.out.println("总差额：24464.7");
        System.out.println("已找到：" + bigDecimal);
        System.out.println("剩余差额:" + new BigDecimal("24464.7").subtract(bigDecimal));
    }

    /**

     等式二：
     --------------------------------------------------------------------------------------------------------------
     2月18日，67.5
     原因：发放撤回，C端出账了，B端没入账, 1.0流水回滚，没有记录。

     select * from lf_account_log where id in(
     439778975810945034,
     439779070300258365,
     439779139019702311,
     439780925726195852,
     439780822646982658,
     439778984400915679,
     439783605785723032,
     439778975810945089,
     439780917136261246,
     439785426851922061
     )

     4月8日 49.8 【专款专用】对商户发放，发放账号已出账，商户没入账
     select * from t_merchant_transaction where out_trans_no = 'CHYX491429496594366559';

     4月18日 1.1 【珑珠统一下单退款】商户出钱了，用户没入账
     select * from t_merchant_transaction where out_trans_no = 'DYZ02202304181218167685222367901';

     4月21日 216.9 【食堂退款，商户出钱了，用户没入账】
     select * from lfm_account_log where order_no = 'R1191681790851697';
     --------------------------------------------------------------------------------------------------------------

     -- 以下是调用postman其中一条记录
     https://api.longhu.net/longem-integrate-prod/api/v1/integrate/baccount/trade

     {
     "amount":49.8,
     "transType":2,
     "businessType":"15022",
     "inAccNo":"FF-230208-01444",
     "inAccNoType":20,
     "outAccNo":"SH-201222-000405",
     "outAccNoType":22,
     "remark":"XM2248301-17554",
     "orderNo":"ref230529100011",
     "requestNo":"CHYX491429496594366559",
     "appId":"113",
     "source":"-100"
     }

     */
}
