package com.thisthat.thisthat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchResultsModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("self_id")
    @Expose
    private Integer selfId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("evaluatorPhone")
    @Expose
    private String evaluatorPhone;
    @SerializedName("evaluateePhone")
    @Expose
    private String evaluateePhone;
    @SerializedName("evaluatorChoice")
    @Expose
    private String evaluatorChoice;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("category")
    @Expose
    private Category category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSelfId() {
        return selfId;
    }

    public void setSelfId(Integer selfId) {
        this.selfId = selfId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getEvaluatorPhone() {
        return evaluatorPhone;
    }

    public void setEvaluatorPhone(String evaluatorPhone) {
        this.evaluatorPhone = evaluatorPhone;
    }

    public String getEvaluateePhone() {
        return evaluateePhone;
    }

    public void setEvaluateePhone(String evaluateePhone) {
        this.evaluateePhone = evaluateePhone;
    }

    public String getEvaluatorChoice() {
        return evaluatorChoice;
    }

    public void setEvaluatorChoice(String evaluatorChoice) {
        this.evaluatorChoice = evaluatorChoice;
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

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
