package com.organically.organically;

public class ViewBidsModel {
    private String bidsproducerPricePerUnit,bidsDescription,farmerProducerId, orderId,itemName,qtyRequiredByCustomer,unit,duration;

    public ViewBidsModel(String bidsproducerPricePerUnit,String bidsDescription,String farmerProducerId,String orderId,String itemName,String qtyRequiredByCustomer,String unit,String duration) {
        this.bidsproducerPricePerUnit = bidsproducerPricePerUnit;
        this.bidsDescription=bidsDescription;
        this.farmerProducerId=farmerProducerId;
        this.orderId=orderId;
        this.itemName=itemName;
        this.qtyRequiredByCustomer=qtyRequiredByCustomer;
        this.unit=unit;
        this.duration=duration;

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFarmerProducerId() {
        return farmerProducerId;
    }

    public void setFarmerProducerId(String farmerProducerId) {
        this.farmerProducerId = farmerProducerId;
    }


    public String getBidsproducerPricePerUnit() {
        return bidsproducerPricePerUnit;
    }

    public void setBidsproducerPricePerUnit(String bidsproducerPricePerUnit) {
        this.bidsproducerPricePerUnit = bidsproducerPricePerUnit;
    }


    public String getBidsDescription() {
        return bidsDescription;
    }

    public void setBidsDescription(String bidsDescription) {
        this.bidsDescription = bidsDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public String getQtyRequiredByCustomer() {
        return qtyRequiredByCustomer;
    }

    public String getUnit() {
        return unit;
    }

    public String getDuration() {
        return duration;
    }
}
