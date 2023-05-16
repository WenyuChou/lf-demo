package com.example.demo.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwenyu
 * date 2023-02-15
 */
public class ExcelUtil {
    public static void main(String[] args) {

        ExcelWriter excelWriter = EasyExcelFactory.write().build();
        List<String[]> l = new ArrayList<>();
        l.add(new String[]{"cell1","cell2"});
        WriteSheet writeSheet = EasyExcelFactory.writerSheet("未占用明细").build();
    }
}
