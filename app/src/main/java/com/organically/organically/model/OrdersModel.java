package com.organically.organically.model;

public class OrdersModel {
    private String orderItem,orderQty,duration,status,unit,customerId,pricePerUnitOffered;

    public OrdersModel( String orderItem, String orderQty,String duration,String status,String unit,String customerId,String pricePerUnitOffered) {
        this.orderItem = orderItem;
        this.orderQty = orderQty;
        this.duration=duration;
        this.status=status;
        this.unit=unit;
        this.customerId=customerId;
        this.pricePerUnitOffered=pricePerUnitOffered;
    }


    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnit() {
        return unit;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getPricePerUnitOffered() {
        return pricePerUnitOffered;
    }
}
