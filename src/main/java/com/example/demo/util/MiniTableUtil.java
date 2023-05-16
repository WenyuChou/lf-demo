package com.example.demo.util;

import com.blinkfox.minitable.MiniTable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwenyu
 * date 2022-09-23
 */
public class MiniTableUtil {
    public static String print(String title, List<String> headers, List<List<Object>> list) {
        MiniTable miniTable = StringUtils.isBlank(title) ? new MiniTable() : new MiniTable(title);
        miniTable.addHeaders(headers);
        list.forEach(miniTable::addDatas);
        return miniTable.render();
    }
    public static void printAuto(String title, List<String> headers, List<List<Object>> list) {
        MiniTable miniTable = StringUtils.isBlank(title) ? new MiniTable() : new MiniTable(title);
        miniTable.addHeaders(headers);
        list.forEach(miniTable::addDatas);
        System.out.println(miniTable.render());
    }
    public static String print(List<String> headers, List<List<Object>> list) {
        return print(null, headers, list);
    }


    public static void main(String[] args) {
        List<List<Object>> datas = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        List<Object> data0 = new ArrayList<>();
        headers.add("name");
        headers.add("sex");
        data0.add("zhangsan");
        data0.add(12);
        datas.add(data0);
        List<Object> data1 = new ArrayList<>();
        data1.add("zhangsanyi");
        data1.add(122);
        datas.add(data1);
        System.out.println(print(headers, datas));
    }
}
