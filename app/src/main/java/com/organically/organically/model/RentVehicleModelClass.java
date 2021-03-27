package com.organically.organically.model;

public class RentVehicleModelClass {
    String picLink,vehicleType,company,model,rent,farmerId,farmerName,requestor;

    public RentVehicleModelClass(String picLink, String vehicleType, String company, String model, String rent, String farmerId,String farmerName,String requestor) {
        this.picLink = picLink;
        this.vehicleType = vehicleType;
        this.company = company;
        this.model = model;
        this.rent = rent;
       this.farmerId=farmerId;
       this.requestor=requestor;
       this.farmerName=farmerName;

    }

    public String getPicLink() {
        return picLink;
    }

    public void setPicLink(String picLink) {
        this.picLink = picLink;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }
}
