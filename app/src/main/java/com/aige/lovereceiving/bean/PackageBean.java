package com.aige.lovereceiving.bean;

public class PackageBean {
    private String id;
    private String orderid;
    private String auditname;
    private String packageCode;
    private String deliverydate;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAuditname() {
        return auditname;
    }

    public void setAuditname(String auditname) {
        this.auditname = auditname;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(String deliverydate) {
        this.deliverydate = deliverydate;
    }

    @Override
    public String toString() {
        return "PackageBean{" +
                "id='" + id + '\'' +
                ", orderid='" + orderid + '\'' +
                ", auditname='" + auditname + '\'' +
                ", packageCode='" + packageCode + '\'' +
                ", deliverydate='" + deliverydate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}