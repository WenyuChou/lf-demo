package com.example.demo.pojo;
import lombok.Data;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-03 11:12:41
 *
 */
@Data
public class GrantBudgetMain implements Serializable {

    private static final long serialVersionUID = -1379277290813187176L;

    /**自增主键*/
    private Long id;
    /**请求编号(上游系统业务数据表主键ID)*/
    private String requestNo;
    /**交易来源*/
    private String source;
    /**业务流水号*/
    private String orderNo;
    /**预算占用金额*/
    private BigDecimal budgetOccupyAmt;
    /**费控类型 0-龙湖费控 1-双湖费控 2-新成本 3-千丁飞鸟鱼*/
    private Integer serialType;
    /**预算占用状态 -1-初始化 0-处理中 1-成功 2-失败*/
    private Integer status;
    /**预算占用类型 0-同步 1-异步*/
    private Integer budgetOccupyType;
    /**发放类型 1-商户 2-个人*/
    private Integer grantType;
    /**预算占用时的批次号*/
    private String budgetBatchNo;
    /**费控单号*/
    private String serialNo;
    /**动作类型 1-发放 2-撤销*/
    private Integer actionType;
    /**业务系统编码*/
    private String appId;
    /**备注*/
    private String remark;
    /**费控系统响应编码*/
    private String budgetResultCode;
    /**费控系统响应描述*/
    private String budgetResultMsg;
    /**归属项目*/
    private String pSsxm;
    /**付款单位编码*/
    private String pFkdw;
    /**付款单位名称*/
    private String pFkdwName;
    /**币种*/
    private String pBz;
    /**制单人*/
    private String pZdr;
    /**申请人*/
    private String pSqr;
    /**申请部门*/
    private String pSsbm;
    /**收款单位*/
    private String pSkdw;
    /**来源系统编号*/
    private String pLyxtid;
    /**来源系统名称*/
    private String pLyxt;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;
    /**预算占用时间*/
    private Date budgetTime;
    /**是否已删除 0-否 1-是*/
    private Integer isDeleted;
    /**order_no,seq建立唯一索引*/
    private Long seq;

}