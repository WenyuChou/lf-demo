package com.example.demo.pojo;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-02-10 10:26:46
 */
public class grantAccountFk implements Serializable {

    private static final long serialVersionUID = 8185747752981253382L;

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 外部请求号
     */
    private String requestNo;
    /**
     * 费控单号
     */
    private String serialNo;
    /**
     * 费控系统类型：0-龙湖费控系统 1-双湖费控系统 2-新成本系统
     */
    private Integer serialType;
    /**
     * 账号
     */
    private String accNo;
    /**
     * 金额-珑珠
     */
    private BigDecimal amount;
    /**
     * 状态：0-未使用，1-已使用
     */
    private Integer status;
    /**
     * 序号
     */
    private Long seq;
    /**
     * 通知费控时间
     */
    private Date notifyTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Integer getSerialType() {
        return serialType;
    }

    public void setSerialType(Integer serialType) {
        this.serialType = serialType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}