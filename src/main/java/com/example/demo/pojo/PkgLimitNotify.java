package com.example.demo.pojo;
import java.io.Serializable;

import java.util.Date;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-03-09 17:52:01
 *
 */
public class PkgLimitNotify implements Serializable {

    private static final long serialVersionUID = 9211198413079690026L;

    /**key*/
    private Long id;
    /**oa*/
    private String oa;
    /**1:发送打赏；2接收打赏*/
    private Integer actionType;
    /**年份：yyyy*/
    private String year;
    /**等级，1-10w，2-30w*/
    private Integer level;
    /**请填入描述*/
    private Date createTime;
    /**请填入描述*/
    private Date updateTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOa() {
        return oa;
    }
    public void setOa(String oa) {
        this.oa = oa;
    }
    public Integer getActionType() {
        return actionType;
    }
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
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
}