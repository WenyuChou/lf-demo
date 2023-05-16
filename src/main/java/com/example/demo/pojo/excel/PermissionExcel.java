package com.example.demo.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author zhouwenyu
 * date 2021-11-09
 */
@Data
public class PermissionExcel {
    @ExcelProperty("permission_url")
    private String permissionUrl;
}
