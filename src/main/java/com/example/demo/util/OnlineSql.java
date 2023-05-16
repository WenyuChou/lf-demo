package com.example.demo.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.excel.ExcelData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;


@Slf4j
public class OnlineSql {

    public String cookie = "EQID=7d427a51a7f6430994fc08a6eea064b1; zg_87d550a769cd4f4995a2da640d7869f7=%7B%22sid%22%3A%201659354930751%2C%22updated%22%3A%201659354932113%2C%22info%22%3A%201659354930753%2C%22superProperty%22%3A%20%22%7B%5C%22%E5%BA%94%E7%94%A8%E5%90%8D%E7%A7%B0%5C%22%3A%20%5C%22%E9%BE%99%E4%BF%A13.0%5C%22%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%2C%22landHref%22%3A%20%22https%3A%2F%2Foriginal-nas-pro.longfor.com%2Foriginal-nas-pro%2FapplicationMedia%2F1637227663740.html%3FExpires%3D1952587666%26OSSAccessKeyId%3DLTAI4Fw2dUgQvkWHnasff6ps%26Signature%3DiAnb9ursRD5lBbMig6hjQTZ7cGc%253D%23%2Fmain%2Fportal%2Fportal-1-1%3F_key%3Dnl8e6k%22%7D; CASTGC=TGT-331427-weP4jQ8ZTvIrTU0QXspsZ-K0mTsqqwgivP-9L03gyjWAO9pxoG228arWmNVf9JuQu6c-longhu; account=TGT-331427-weP4jQ8ZTvIrTU0QXspsZ-K0mTsqqwgivP-9L03gyjWAO9pxoG228arWmNVf9JuQu6c-longhu; sessionid=qp6l7mvs54hxxdo6qiq3v37f67wy85xl";

    @Getter
    @Setter
    private List<String> columns;
    @Getter
    @Setter
    private List<JSONObject> result;

    public HttpRequest initRequest() {
        Map<String, List<String>> head = new HashMap<>();
        head.put("Accept", Collections.singletonList("application/json, text/javascript, */*; q=0.01"));
        head.put("Accept-Encoding", Collections.singletonList("gzip, deflate"));
        head.put("Accept-Language", Collections.singletonList("zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6"));
        head.put("Content-Type", Collections.singletonList("application/x-www-form-urlencoded; charset=UTF-8"));
        head.put("X-CSRFToken", Collections.singletonList(null));
        head.put("X-Requested-With", Collections.singletonList("XMLHttpRequest"));
        head.put("Cookie", Collections.singletonList(cookie));
        return HttpUtil.createPost("http://dbaexecsql.longfor.com/data_application/app_query/").header(head);
    }

    public List<JSONObject> querySql(String sql) {
        Map<String, Object> param = new HashMap<>();
        param.put("instance_name", "tidb_10.31.48.7_脱敏查询");
        param.put("db_name", "longem_tidb");
        param.put("limit_num", 10000);
        param.put("sql_content", sql);
        HttpRequest request = this.initRequest().form(param);
        HttpResponse execute = request.execute();
        JSONObject httpResult = JSON.parseObject(execute.body()).getJSONObject("data");
        String msg = JSON.parseObject(execute.body()).getString("msg");
        if (!"ok".equalsIgnoreCase(msg)) {
            System.out.println(msg);
        }
        JSONArray rows = httpResult.getJSONArray("rows");
        JSONArray column_list = httpResult.getJSONArray("column_list");
        this.columns = column_list.toJavaList(String.class);
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            JSONObject obj = new JSONObject();
            for (int j = 0; j < rows.getJSONArray(i).size(); j++) {
                obj.put(column_list.getString(j), rows.getJSONArray(i).getObject(j, Object.class));
            }
            list.add(obj);
        }
        return list;
    }

    public void printResult(List<JSONObject> list, String column) {
        this.printResult(Collections.singletonList(column), list);
    }

    public void printResult(List<String> column, List<JSONObject> list) {
        List<List<Object>> datas = new ArrayList<>();
        for (JSONObject jsonObject : list) {
            List<Object> data = new ArrayList<>();
            for (String key : column) {
                data.add(jsonObject.getObject(key, Object.class));
            }
            datas.add(data);
        }
        System.out.println("\n\n\n" + MiniTableUtil.print("SQL查询结果", column, datas));
    }

    public static void main(String[] args) {
        String[] split = accList.split("\n");
        List<ExcelData> dataList = new ArrayList<>();
        OnlineSql onlineSql = new OnlineSql();
        int count = 0;
        for (String s : split) {
            ExcelData data = new ExcelData();
            data.setS1(s);
            String sql1 = "select info.acc_no,info.acc_name,info.app_id from lfm_account_info info where info.acc_no = '" + s + "'";
            List<JSONObject> json = onlineSql.querySql(sql1);
            data.setS2(json.get(0).getString("acc_name"));
            data.setS3(json.get(0).getString("app_id"));
            data.setS4("发放账户");
            //充值
            sql1 = "select sum(trans_amount/10) as 'czje' from lfm_account_log where acc_no = '" + s + "' and create_time>='2022-12-01' and create_time < '2022-12-27' and other_account_type = 25 and business_type = '001';";
            json = onlineSql.querySql(sql1);
            data.setS5(Optional.ofNullable(json.get(0).getBigDecimal("czje")).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            //发放
            sql1 = "select sum(grant_amount/10) as ffje from grant_order where create_time>='2022-12-01' and create_time < '2022-12-27' and `status` = 1 and out_acc_no = '" + s + "'";
            json = onlineSql.querySql(sql1);
            data.setS6(Optional.ofNullable(json.get(0).getBigDecimal("ffje")).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            //撤回
            sql1 = "select sum(rf.amount/10) as chje from grant_refund_order rf inner join grant_order g on rf.refund_no = g.request_no where rf.create_time>='2022-12-01' and rf.create_time < '2022-12-27' and rf.`status` = 1 and g.out_acc_no = '" + s + "'";
            json = onlineSql.querySql(sql1);
            data.setS7(Optional.ofNullable(json.get(0).getBigDecimal("chje")).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            //回充
            sql1 = "select sum(trans_amount/10) hcje from lfm_account_log where create_time>='2022-12-01' and create_time < '2022-12-27' and other_account_type = 25 and action_type = 2 and acc_no = '" + s + "'";
            json = onlineSql.querySql(sql1);
            data.setS8(Optional.ofNullable(json.get(0).getBigDecimal("hcje")).orElse(BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            dataList.add(data);
            System.out.println(++count);
        }
        String fileName = "/Users/admin/Desktop/导出文件.xlsx";
        EasyExcel.write(fileName, ExcelData.class).sheet("导出数据").doWrite(dataList);
    }

    static String accList = "FF-220101-40813\n" +
            "FF-211129-39750\n" +
            "FF-210622-08729\n" +
            "FF-210611-08394\n" +
            "FF-211223-40607\n" +
            "FF-211015-34246\n" +
            "FF-211021-38177\n" +
            "FF-210604-08290\n" +
            "FF-210621-08723\n" +
            "FF-211027-39271\n" +
            "FF-210611-08408\n" +
            "FF-220106-40859\n" +
            "FF-210608-08328\n" +
            "FF-210611-08407\n" +
            "FF-210810-11104\n" +
            "FF-210625-08768\n" +
            "FF-210609-08338\n" +
            "FF-210609-08355\n" +
            "FF-210630-08879\n" +
            "FF-210922-28894\n" +
            "FF-210824-11782\n" +
            "FF-210807-11058\n" +
            "FF-210612-08419\n" +
            "FF-210623-08737\n" +
            "FF-210610-08363\n" +
            "FF-210618-08694\n" +
            "FF-210609-08354\n" +
            "FF-220111-40903\n" +
            "FF-220112-40906\n" +
            "FF-220106-40851\n" +
            "FF-210609-08340\n" +
            "FF-220106-40854\n" +
            "FF-220107-40870\n" +
            "FF-210924-29094\n" +
            "FF-220106-40865\n" +
            "FF-210612-08415\n" +
            "FF-211213-40352\n" +
            "FF-210628-08799\n" +
            "FF-220112-40941\n" +
            "FF-210624-08762\n" +
            "FF-210704-08921\n" +
            "FF-210618-08699\n" +
            "FF-211027-39275\n" +
            "FF-210630-08881\n" +
            "FF-211204-40211\n" +
            "FF-210625-08774\n" +
            "FF-210609-08334\n" +
            "FF-210627-08794\n" +
            "FF-210804-10892\n" +
            "FF-211019-35281\n" +
            "FF-210625-08773\n" +
            "FF-220123-41244\n" +
            "FF-210618-08696\n" +
            "FF-210726-09487\n" +
            "FF-211215-40383\n" +
            "FF-210910-27336\n" +
            "FF-210913-27709\n" +
            "FF-210913-27833\n" +
            "FF-210913-27818\n" +
            "FF-210708-08968\n" +
            "FF-210927-29175\n" +
            "FF-220111-40900\n" +
            "FF-210611-08412\n" +
            "FF-210909-27100\n" +
            "FF-210802-10849\n" +
            "FF-220120-41197\n" +
            "FF-210627-08793\n" +
            "FF-210610-08361\n" +
            "FF-211119-39644\n" +
            "FF-210813-11197\n" +
            "FF-220115-41055\n" +
            "FF-210925-29118\n" +
            "FF-210905-12149\n" +
            "FF-210701-08891\n" +
            "FF-210528-08137\n" +
            "FF-220114-41008\n" +
            "FF-210614-08595\n" +
            "FF-210611-08387\n" +
            "FF-220124-41263\n" +
            "FF-220118-41152\n" +
            "FF-210826-11961\n" +
            "FF-220118-41148\n" +
            "FF-210708-08963\n" +
            "FF-211001-29744\n" +
            "FF-210805-10901\n" +
            "FF-220118-41146\n" +
            "FF-210625-08763\n" +
            "FF-210611-08411\n" +
            "FF-220124-41247\n" +
            "FF-210708-08972\n" +
            "FF-210623-08745\n" +
            "FF-210529-08171\n" +
            "FF-220123-41245\n" +
            "FF-210626-08784\n" +
            "FF-210610-08362\n" +
            "FF-220121-41209\n" +
            "FF-210621-08716\n" +
            "FF-210609-08342\n" +
            "FF-220120-41189\n" +
            "FF-210624-08757\n" +
            "FF-210709-08977\n" +
            "FF-210618-08685\n" +
            "FF-210625-08771\n" +
            "FF-210711-09000\n" +
            "FF-210813-11196\n" +
            "FF-210620-08712\n" +
            "FF-220120-41196\n" +
            "FF-211027-39278\n" +
            "FF-210709-08975\n" +
            "FF-210508-07486\n" +
            "FF-211128-39741\n" +
            "FF-210827-11981\n" +
            "FF-210613-08432\n" +
            "FF-220126-42113\n" +
            "FF-210621-08725\n" +
            "FF-210610-08358\n" +
            "FF-211022-39220\n" +
            "FF-220130-42169\n" +
            "FF-210620-08714\n" +
            "FF-210723-09437\n" +
            "FF-210520-07678\n" +
            "FF-220205-42173\n" +
            "FF-210606-08305\n" +
            "FF-220207-42344\n" +
            "FF-211007-30154\n" +
            "FF-210618-08692\n" +
            "FF-220206-42174\n" +
            "FF-210817-11232\n" +
            "FF-220208-42358\n" +
            "FF-210827-11980\n" +
            "FF-220207-42342\n" +
            "FF-210609-08339\n" +
            "FF-210928-29235\n" +
            "FF-210929-29441\n" +
            "FF-210719-09184\n" +
            "FF-220209-42400\n" +
            "FF-210609-08333\n" +
            "FF-210610-08381\n" +
            "FF-210826-11963\n" +
            "FF-210706-08939\n" +
            "FF-210901-12101\n" +
            "FF-210625-08770\n" +
            "FF-210616-08624\n" +
            "FF-210810-11098\n" +
            "FF-211020-37203\n" +
            "FF-211029-39330\n" +
            "FF-210616-08615\n" +
            "FF-210612-08420\n" +
            "FF-220211-42455\n" +
            "FF-220211-42456\n" +
            "FF-220213-42480\n" +
            "FF-220213-42479\n" +
            "FF-211125-39712\n" +
            "FF-210715-09100\n" +
            "FF-210714-09075\n" +
            "FF-220215-42487\n" +
            "FF-220212-42476\n" +
            "FF-220213-42478\n" +
            "FF-210930-29532\n" +
            "FF-210528-08128\n" +
            "FF-220212-42477\n" +
            "FF-211124-39699\n" +
            "FF-210616-08613\n" +
            "FF-210909-27119\n" +
            "FF-210723-09414\n" +
            "FF-220213-42481\n" +
            "FF-211031-39357\n" +
            "FF-220214-42484\n" +
            "FF-210805-10895\n" +
            "FF-210726-09486\n" +
            "FF-210617-08645\n" +
            "FF-210909-27063\n" +
            "FF-210524-07757\n" +
            "FF-210512-07530\n" +
            "FF-210430-07410\n" +
            "FF-211128-39740\n" +
            "FF-220221-42558\n" +
            "FF-210922-28887\n" +
            "FF-210913-27901\n" +
            "FF-211026-39256\n" +
            "FF-211110-39476\n" +
            "FF-220218-42545\n" +
            "FF-210618-08700\n" +
            "FF-210617-08643\n" +
            "FF-220217-42512\n" +
            "FF-220217-42516\n" +
            "FF-220216-42505\n" +
            "FF-210707-08955\n" +
            "FF-220217-42514\n" +
            "FF-210918-28483\n" +
            "FF-220218-42547\n" +
            "FF-210716-09156\n" +
            "FF-220218-42541\n" +
            "FF-210611-08388\n" +
            "FF-220218-42543\n" +
            "FF-210519-07653\n" +
            "FF-220218-42548\n" +
            "FF-220219-42552\n" +
            "FF-210430-07415\n" +
            "FF-210514-07552\n" +
            "FF-210626-08782\n" +
            "FF-210716-09155\n" +
            "FF-220224-42583\n" +
            "FF-210520-07683\n" +
            "FF-210521-07723\n" +
            "FF-220224-42586\n" +
            "FF-220224-42587\n" +
            "FF-210610-08379\n" +
            "FF-210622-08731\n" +
            "FF-211022-39230\n" +
            "FF-210613-08435\n" +
            "FF-210521-07724\n" +
            "FF-210902-12126\n" +
            "FF-211019-35285\n" +
            "FF-210709-08981\n" +
            "FF-210618-08690\n" +
            "FF-210525-08084\n" +
            "FF-210525-08059\n" +
            "FF-210609-08332\n" +
            "FF-210729-09660\n" +
            "FF-210909-27056\n" +
            "FF-210609-08346\n" +
            "FF-220226-42600\n" +
            "FF-220304-42699\n" +
            "FF-220226-42602\n" +
            "FF-220301-42640\n" +
            "FF-211208-40282\n" +
            "FF-210517-07562\n" +
            "FF-220301-42642\n" +
            "FF-211006-29909\n" +
            "FF-210720-09288\n" +
            "FF-220304-42704\n" +
            "FF-220304-42702\n" +
            "FF-210609-08335\n" +
            "FF-220305-42706\n" +
            "FF-210518-07632\n" +
            "FF-210625-08778\n" +
            "FF-210802-10853\n" +
            "FF-211124-39698\n" +
            "FF-220307-42724\n" +
            "FF-220411-43325\n" +
            "FF-220308-42916\n" +
            "FF-210505-07448\n" +
            "FF-210804-10885\n" +
            "FF-210909-27088\n" +
            "FF-220314-42963\n" +
            "FF-211020-37215\n" +
            "FF-211224-40666\n" +
            "FF-220312-42958\n" +
            "FF-211015-35101\n" +
            "FF-210816-11224\n" +
            "FF-210611-08385\n" +
            "FF-210511-07518\n" +
            "FF-220313-42960\n" +
            "FF-210810-11092\n" +
            "FF-210705-08925\n" +
            "FF-210916-28171\n" +
            "FF-210922-28896\n" +
            "FF-210507-07467\n" +
            "FF-220316-42997\n" +
            "FF-210722-09382\n" +
            "FF-210903-12140\n" +
            "FF-210616-08622\n" +
            "FF-210930-29573\n" +
            "FF-210612-08417\n" +
            "FF-210629-08822\n" +
            "FF-210913-27706\n" +
            "FF-220321-43073\n" +
            "FF-210705-08930\n" +
            "FF-210530-08175\n" +
            "FF-210621-08717\n" +
            "FF-210525-08075\n" +
            "FF-220325-43131\n" +
            "FF-210506-07456\n" +
            "FF-220324-43100\n" +
            "FF-211130-39789\n" +
            "FF-210612-08422\n" +
            "FF-210610-08376\n" +
            "FF-211002-29783\n" +
            "FF-211231-40806\n" +
            "FF-210613-08430\n" +
            "FF-220323-43094\n" +
            "FF-211016-35128\n" +
            "FF-211220-40520\n" +
            "FF-220327-43141\n" +
            "FF-220325-43120\n" +
            "FF-210604-08293\n" +
            "FF-220402-43287\n" +
            "FF-220330-43176\n" +
            "FF-211213-40350\n" +
            "FF-220328-43156\n" +
            "FF-220328-43157\n" +
            "FF-220325-43136\n" +
            "FF-220326-43139\n" +
            "FF-210704-08920\n" +
            "FF-220329-43171\n" +
            "FF-210518-07621\n" +
            "FF-210929-29451\n" +
            "FF-210927-29191\n" +
            "FF-210531-08214\n" +
            "FF-220418-43402\n" +
            "FF-220402-43282\n" +
            "FF-220402-43286\n" +
            "FF-220402-43284\n" +
            "FF-220406-43298\n" +
            "FF-210916-28172\n" +
            "FF-210729-09657\n" +
            "FF-210721-09373\n" +
            "FF-210811-11161\n" +
            "FF-210521-07725\n" +
            "FF-220408-43320\n" +
            "FF-220408-43309\n" +
            "FF-220408-43310\n" +
            "FF-210528-08148\n" +
            "FF-210528-08140\n" +
            "FF-210519-07650\n" +
            "FF-210530-08176\n" +
            "FF-220413-43342\n" +
            "FF-220414-43350\n" +
            "FF-210705-08931\n" +
            "FF-210813-11198\n" +
            "FF-220414-43347\n" +
            "FF-211021-39204\n" +
            "FF-211013-34186\n" +
            "FF-210830-12016\n" +
            "FF-210609-08331\n" +
            "FF-220415-43358\n" +
            "FF-220416-43365\n" +
            "FF-210524-07750\n" +
            "FF-210717-09166\n" +
            "FF-211117-39583\n" +
            "FF-210610-08360\n" +
            "FF-210726-09506\n" +
            "FF-210528-08142\n" +
            "FF-220420-43425\n" +
            "FF-220419-43418\n" +
            "FF-220419-43414\n" +
            "FF-220421-43436\n" +
            "FF-220422-43438\n" +
            "FF-220424-43471\n" +
            "FF-220424-43460\n" +
            "FF-210615-08606\n" +
            "FF-210520-07698\n" +
            "FF-210606-08307\n" +
            "FF-210623-08743\n" +
            "FF-210707-08959\n" +
            "FF-211113-39526\n" +
            "FF-210714-09077\n" +
            "FF-210812-11175\n" +
            "FF-220428-43510\n" +
            "FF-220427-43497\n" +
            "FF-210512-07528\n" +
            "FF-211118-39625\n" +
            "FF-211028-39285\n" +
            "FF-210626-08783\n" +
            "FF-220430-43574\n" +
            "FF-220430-43577\n" +
            "FF-210521-07712\n" +
            "FF-210830-12015\n" +
            "FF-220524-43853\n" +
            "FF-220508-43631\n" +
            "FF-211027-39283\n" +
            "FF-210705-08929\n" +
            "FF-220508-43626\n" +
            "FF-220510-43688\n" +
            "FF-220509-43682\n" +
            "FF-220522-43829\n" +
            "FF-210918-28449\n" +
            "FF-220513-43742\n" +
            "FF-220512-43728\n" +
            "FF-210530-08178\n" +
            "FF-220514-43751\n" +
            "FF-210430-07433\n" +
            "FF-210705-08926\n" +
            "FF-220517-43776\n" +
            "FF-220524-43850\n" +
            "FF-220523-43847\n" +
            "FF-220520-43817\n" +
            "FF-210609-08356\n" +
            "FF-210519-07640\n" +
            "FF-211110-39475\n" +
            "FF-210429-07374\n" +
            "FF-220526-43879\n" +
            "FF-210624-08756\n" +
            "FF-220525-43877\n" +
            "FF-220528-43897\n" +
            "FF-220601-43952\n" +
            "FF-210706-08949\n" +
            "FF-220601-43951\n" +
            "FF-210616-08617\n" +
            "FF-220602-43959\n" +
            "FF-210730-10728";
}
