package com.organically.organically;

public class MyBidsFarmersProducersModel {
    private String orderTitle,orderItemName,orderQty,areaOrPincode,orderDuration,mybidPricePerUnit,mybidDescription,customerId,orderId;

    public MyBidsFarmersProducersModel(String orderTitle, String orderItemName, String orderQty, String areaOrPincode, String orderDuration, String mybidPricePerUnit, String mybidDescription,String customerId,String orderId) {
        this.orderTitle = orderTitle;
        this.orderItemName = orderItemName;
        this.orderQty = orderQty;
        this.areaOrPincode = areaOrPincode;
        this.orderDuration = orderDuration;
        this.mybidPricePerUnit = mybidPricePerUnit;
        this.mybidDescription = mybidDescription;
        this.customerId=customerId;
        this.orderId=orderId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getAreaOrPincode() {
        return areaOrPincode;
    }

    public void setAreaOrPincode(String areaOrPincode) {
        this.areaOrPincode = areaOrPincode;
    }

    public String getOrderDuration() {
        return orderDuration;
    }

    public void setOrderDuration(String orderDuration) {
        this.orderDuration = orderDuration;
    }

    public String getMybidPricePerUnit() {
        return mybidPricePerUnit;
    }

    public void setMybidPricePerUnit(String mybidPricePerUnit) {
        this.mybidPricePerUnit = mybidPricePerUnit;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMybidDescription() {
        return mybidDescription;
    }

    public void setMybidDescription(String mybidDescription) {
        this.mybidDescription = mybidDescription;
    }

    public String getOrderId() {
        return orderId;
    }
}

