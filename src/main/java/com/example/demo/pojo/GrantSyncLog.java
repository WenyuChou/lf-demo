package com.example.demo.pojo;
import java.io.Serializable;

import java.util.Date;


/**
 * @author zhouwenyu@longfor.com
 * @version 1.0
 * @date 2023-04-12 15:30:40
 *
 */
public class GrantSyncLog implements Serializable {

    private static final long serialVersionUID = 4684858824827808523L;

    /**主键*/
    private Long id;
    /**同步类型*/
    private String syncType;
    /**来源表*/
    private String tableName;
    /**来源表id*/
    private Long tableId;
    /**0-未同步，1-已同步*/
    private Integer status;
    /**创建时间*/
    private Date createTime;
    /**更新时间*/
    private Date updateTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSyncType() {
        return syncType;
    }
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public Long getTableId() {
        return tableId;
    }
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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