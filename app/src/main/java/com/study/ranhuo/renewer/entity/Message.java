/**
 * # CSIT 6000B # HUO Ran 20295065 rhuo@connect.ust.hk
 * # CSIT 6000B # CHEN Daoyuan 20292166 dchenan@connect.ust.hk
 * # CSIT 6000B # Liu Zhuoling 20297154 zliubk@connect.ust.hk
 */
package com.study.ranhuo.renewer.entity;


public class Message {
    private String sender;
    private String time;
    private String goodsTitle;
    private String goodsId;
    private String goodsPublisher;
    private String goodsPrice;
    private String goodsDescription;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsPublisher() {
        return goodsPublisher;
    }

    public void setGoodsPublisher(String goodsPublisher) {
        this.goodsPublisher = goodsPublisher;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }
}
