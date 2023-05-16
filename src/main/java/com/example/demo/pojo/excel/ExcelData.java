package com.example.demo.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouwenyu
 * date 2022-12-14
 */
@Data
public class ExcelData {
    @ExcelProperty("账户编码")
    private String s1;
    @ExcelProperty("账户名称（中文）")
    private String s2;
    @ExcelProperty("APPID")
    private String s3;
    @ExcelProperty("账户类型（中文）")
    private String s4;
    @ExcelProperty("年度累计充值（元）")
    private BigDecimal s5;
    @ExcelProperty("年度累计发放（元）")
    private BigDecimal s6;
    @ExcelProperty("年度累计撤回（元）")
    private BigDecimal s7;
    @ExcelProperty("年度累计回充（元）")
    private BigDecimal s8;

}
