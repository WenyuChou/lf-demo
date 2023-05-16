package com.example.demo.pojo;
import java.io.Serializable;

import java.util.Date;
import java.math.BigDecimal;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-05-16 10:27:46
 *
 */
public class StatisticsFk implements Serializable {

    private static final long serialVersionUID = -4277974340105106333L;

    /**请填入描述*/
    private Integer id;
    /**费控单号*/
    private String serialNumber;
    /**预算占用时间*/
    private Date budgetOccupyTime;
    /**预算占用金额*/
    private BigDecimal budgetOccupyAmt;
    /**申请人OA*/
    private String personAd;
    /**请填入描述*/
    private String bizSysId;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date modifyTime;
    /**来源系统是否为费控 1：是费控 2：不是费控*/
    private Integer fkFlag;
    /**已发放金额*/
    private BigDecimal budgetGrantAmt;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public Date getBudgetOccupyTime() {
        return budgetOccupyTime;
    }
    public void setBudgetOccupyTime(Date budgetOccupyTime) {
        this.budgetOccupyTime = budgetOccupyTime;
    }
    public BigDecimal getBudgetOccupyAmt() {
        return budgetOccupyAmt;
    }
    public void setBudgetOccupyAmt(BigDecimal budgetOccupyAmt) {
        this.budgetOccupyAmt = budgetOccupyAmt;
    }
    public String getPersonAd() {
        return personAd;
    }
    public void setPersonAd(String personAd) {
        this.personAd = personAd;
    }
    public String getBizSysId() {
        return bizSysId;
    }
    public void setBizSysId(String bizSysId) {
        this.bizSysId = bizSysId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Integer getFkFlag() {
        return fkFlag;
    }
    public void setFkFlag(Integer fkFlag) {
        this.fkFlag = fkFlag;
    }
    public BigDecimal getBudgetGrantAmt() {
        return budgetGrantAmt;
    }
    public void setBudgetGrantAmt(BigDecimal budgetGrantAmt) {
        this.budgetGrantAmt = budgetGrantAmt;
    }
}