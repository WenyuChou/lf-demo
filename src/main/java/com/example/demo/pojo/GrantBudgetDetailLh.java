package com.example.demo.pojo;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-03 11:12:42
 *
 */
@Data
public class GrantBudgetDetailLh implements Serializable {

    private static final long serialVersionUID = -7298545291457252209L;

    /**自增主键*/
    private Long id;
    /**主表ID*/
    private Long parentId;
    /**预算分摊组织编码*/
    private String pYsftbm;
    /**预算分摊组织名称*/
    private String pYsftbmName;
    /**费项*/
    private String pFx;
    /**预算类型*/
    private String pYslx;
    /**费用类型*/
    private String pFylx;
    /**分摊金额*/
    private BigDecimal pFtje;
    /**记录时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;

}