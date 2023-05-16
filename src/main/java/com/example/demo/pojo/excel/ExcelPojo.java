package com.example.demo.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zhouwenyu
 * date 2021-11-09
 */
@Data
public class ExcelPojo {
    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty("接口全路径")
    private String url;
    /**
     * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
     */
    @ExcelProperty("是否在龙珠后台功能excel中")
    private String inExcel;

}
