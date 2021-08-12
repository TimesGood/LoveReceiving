package com.aige.lovereceiving.bean;

public class ReceivingBean {
    private String orderId;
    private String detailName;
    private String packageCode;
    private String receivingDate;
    private String scanType;
    private String scanStatus = "确认收货";

    public String getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus) {
        this.scanStatus = scanStatus;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(String receivingDate) {
        this.receivingDate = receivingDate;
    }

    @Override
    public String toString() {
        return "ReceivingBean{" +
                "orderId='" + orderId + '\'' +
                ", detailName='" + detailName + '\'' +
                ", packageCode='" + packageCode + '\'' +
                ", receivingDate='" + receivingDate + '\'' +
                ", scanType='" + scanType + '\'' +
                '}';
    }
}
