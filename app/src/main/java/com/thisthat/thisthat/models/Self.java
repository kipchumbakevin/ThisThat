package com.thisthat.thisthat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Self {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("selfPhone")
    @Expose
    private String selfPhone;
    @SerializedName("selfChoice")
    @Expose
    private String selfChoice;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSelfPhone() {
        return selfPhone;
    }

    public void setSelfPhone(String selfPhone) {
        this.selfPhone = selfPhone;
    }

    public String getSelfChoice() {
        return selfChoice;
    }

    public void setSelfChoice(String selfChoice) {
        this.selfChoice = selfChoice;
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
