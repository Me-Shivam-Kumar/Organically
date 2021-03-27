package com.organically.organically.model;

public class SearchFarmersProducersModel {
    private String name,address,offerings,pic,aadharNum;


    public SearchFarmersProducersModel(String name, String address, String offerings, String pic,String aadharNum) {
        this.name = name;
        this.address = address;
        this.offerings = offerings;
        this.pic = pic;
        this.aadharNum=aadharNum;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getOfferings() {
        return offerings;
    }

    public String getPic() {
        return pic;
    }

    public String getAadharNum() {
        return aadharNum;
    }
}
