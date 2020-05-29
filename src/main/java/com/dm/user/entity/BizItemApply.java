package com.dm.user.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * app申请公证表(BizItemApply)实体类
 *
 * @author makejava
 * @since 2020-05-28 17:34:23
 */
public class BizItemApply implements Serializable {
    private static final long serialVersionUID = -51225840416049203L;
    /**
    * 申请标识
    */
    private Integer applyid;
    /**
    * 业务编号,  生成规则待定
    */
    private String applyNo;
    /**
    * 项目标识
    */
    private Integer itemid;
    
    private Integer userid;
    /**
    * 状态管理
0 待接单
1 草稿
2 提交已订单(待支付)
3 材料接收
4 材料核查
5 公正意见复核
6 制证
7 意见书送达中
8 公正完成
9 公正失败
10 材料初审未通过
    */
    private Integer status;
    /**
    * 用户申请时的附加说明
    */
    private String describe;
    /**
    * 相关连的存证 id
    */
    private Integer certid;
    
    private Date createdDate;
    
    private Date submitDate;
    /**
    * 标的价格
    */
    private Double itemValue;
    /**
    * 收费价格 当计费方式为2时，用item_value和biz_org_items.price进行计算， 当计费方式为3时，由公证人员输入 
    */
    private Double price;
    /**
    * 接单时间
    */
    private Date handleCreatedDate;
    /**
    * 接单人
    */
    private Integer handleUserid;
    
    private String wfInstanceId;
    /**
    * 0默认 1部分收款 2 待结清尾款 3 结清费用 
    */
    private Integer payStatus;
    /**
    * 待支付金额
    */
    private Double payEndPrice;
    /**
    * 尾款支付时间
    */
    private Date payEndDate;
    /**
    * 项目唯一标识
    */
    private String itemCode;
    /**
    * 材料预审退回原因
    */
    private String rejectReason;
    /**
    * 补充修改材料状态字段(0不需要补充 默认值 1 需要补充)
    */
    private Integer addFileStatus;


    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getCertid() {
        return certid;
    }

    public void setCertid(Integer certid) {
        this.certid = certid;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getHandleCreatedDate() {
        return handleCreatedDate;
    }

    public void setHandleCreatedDate(Date handleCreatedDate) {
        this.handleCreatedDate = handleCreatedDate;
    }

    public Integer getHandleUserid() {
        return handleUserid;
    }

    public void setHandleUserid(Integer handleUserid) {
        this.handleUserid = handleUserid;
    }

    public String getWfInstanceId() {
        return wfInstanceId;
    }

    public void setWfInstanceId(String wfInstanceId) {
        this.wfInstanceId = wfInstanceId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Double getPayEndPrice() {
        return payEndPrice;
    }

    public void setPayEndPrice(Double payEndPrice) {
        this.payEndPrice = payEndPrice;
    }

    public Date getPayEndDate() {
        return payEndDate;
    }

    public void setPayEndDate(Date payEndDate) {
        this.payEndDate = payEndDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Integer getAddFileStatus() {
        return addFileStatus;
    }

    public void setAddFileStatus(Integer addFileStatus) {
        this.addFileStatus = addFileStatus;
    }

}