package com.thisthat.thisthat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WouldYouRatherModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("optionA")
    @Expose
    private String optionA;
    @SerializedName("optionB")
    @Expose
    private String optionB;
    @SerializedName("pickA")
    @Expose
    private Integer pickA;
    @SerializedName("pickB")
    @Expose
    private Integer pickB;
    @SerializedName("total")
    @Expose
    private Integer total;
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

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public Integer getPickA() {
        return pickA;
    }

    public void setPickA(Integer pickA) {
        this.pickA = pickA;
    }

    public Integer getPickB() {
        return pickB;
    }

    public void setPickB(Integer pickB) {
        this.pickB = pickB;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
