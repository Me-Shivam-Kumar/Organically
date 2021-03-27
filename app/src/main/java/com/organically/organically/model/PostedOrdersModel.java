package com.organically.organically.model;

public class PostedOrdersModel {
    private String title,itemName,qty,areaPincode,startDate,customer_id,order_id,endDate,qtyUnit;
    private int requestor;

    public PostedOrdersModel(String title, String itemName, String qty, String areaPincode, String startDate,int requestor,String customer_id,String order_id,String endDate,String qtyUnit) {
        this.title = title;
        this.itemName = itemName;
        this.qty = qty;
        this.areaPincode = areaPincode;
        this.startDate = startDate;
        this.requestor=requestor;
        this.customer_id=customer_id;
        this.order_id=order_id;
        this.endDate=endDate;
        this.qtyUnit=qtyUnit;

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getRequestor() {
        return requestor;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setRequestor(int requestor) {
        this.requestor = requestor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAreaPincode() {
        return areaPincode;
    }

    public void setAreaPincode(String areaPincode) {
        this.areaPincode = areaPincode;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getQtyUnit() {
        return qtyUnit;
    }
}
