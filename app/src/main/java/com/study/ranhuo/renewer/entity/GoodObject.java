/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.entity;

import java.util.List;


public class GoodObject extends Object {
    private String objectid;
    private String username;
    private String price;
    private List<String> imageURL;
    private String discription;
    private String title;
    private Boolean buyFlag;

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getURLList() {
        return imageURL;
    }

    public void setURLList(List<String> bitmapList) {
        this.imageURL = bitmapList;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getBuyFlag() {
        return buyFlag;
    }

    public void setBuyFlag(Boolean buyFlag) {
        this.buyFlag = buyFlag;
    }
}
