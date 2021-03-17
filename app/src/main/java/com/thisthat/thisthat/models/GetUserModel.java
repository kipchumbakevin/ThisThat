package com.thisthat.thisthat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("pin")
    @Expose
    private Integer pin;
    @SerializedName("lifestyle")
    @Expose
    private Integer lifestyle;
    @SerializedName("food")
    @Expose
    private Integer food;
    @SerializedName("celebrity")
    @Expose
    private Integer celebrity;
    @SerializedName("partner")
    @Expose
    private Integer partner;
    @SerializedName("complete")
    @Expose
    private Integer complete;
    @SerializedName("would")
    @Expose
    private Integer would;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(Integer lifestyle) {
        this.lifestyle = lifestyle;
    }

    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    public Integer getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(Integer celebrity) {
        this.celebrity = celebrity;
    }

    public Integer getPartner() {
        return partner;
    }

    public void setPartner(Integer partner) {
        this.partner = partner;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getWould() {
        return would;
    }

    public void setWould(Integer would) {
        this.would = would;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
