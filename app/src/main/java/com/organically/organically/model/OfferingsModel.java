package com.organically.organically.model;

public class OfferingsModel {
    private String offeringName,offeringPricePerUnit,duration,totalQty,startDate,endDate,unit,farmerAadharCardNumber,customerAadharCardNumber;
    int requestor;

    public OfferingsModel(String offeringName, String offeringPricePerUnit, String duration, String totalQty,String startDate,String endDate,String unit, int requestor,String farmerAadharCardNumber,String customerAadharCardNumber ) {
        this.offeringName = offeringName;
        this.offeringPricePerUnit = offeringPricePerUnit;
        this.duration = duration;
        this.totalQty = totalQty;
        this.startDate=startDate;
        this.unit=unit;
        this.endDate=endDate;
        this.requestor=requestor;
        this.farmerAadharCardNumber=farmerAadharCardNumber;
        this.customerAadharCardNumber=customerAadharCardNumber;
    }

    public String getOfferingName() {
        return offeringName;
    }

    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    public String getOfferingPricePerUnit() {
        return offeringPricePerUnit;
    }

    public void setOfferingPricePerUnit(String offeringPricePerUnit) {
        this.offeringPricePerUnit = offeringPricePerUnit;
    }

    public String getFarmerAadharCardNumber() {
        return farmerAadharCardNumber;
    }

    public String getCustomerAadharCardNumber() {
        return customerAadharCardNumber;
    }

    public void setFarmerAadharCardNumber(String farmerAadharCardNumber) {
        this.farmerAadharCardNumber = farmerAadharCardNumber;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getUnit() {
        return unit;
    }

    public int getRequestor() {
        return requestor;
    }
}
