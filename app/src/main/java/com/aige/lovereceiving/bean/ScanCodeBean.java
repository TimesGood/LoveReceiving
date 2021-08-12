package com.aige.lovereceiving.bean;

public class ScanCodeBean {
            private String rowId;
            private String solutionName;
            private String order_Id;
            private String packageCode;
            private String status;
            private String createDate;
            private String inDate;
            private String deliveryDate;
            private String statuss;

    public String getStatuss() {
        return statuss;
    }

    public void setStatuss(String statuss) {
        this.statuss = statuss;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "ScanCodeBean{" +
                "rowId='" + rowId + '\'' +
                ", solutionName='" + solutionName + '\'' +
                ", order_Id='" + order_Id + '\'' +
                ", packageCode='" + packageCode + '\'' +
                ", status='" + status + '\'' +
                ", createDate='" + createDate + '\'' +
                ", inDate='" + inDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", statuss='" + statuss + '\'' +
                '}';
    }
}
