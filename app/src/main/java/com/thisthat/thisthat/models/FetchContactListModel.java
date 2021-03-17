package com.thisthat.thisthat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchContactListModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("self_id")
    @Expose
    private Integer selfId;
    @SerializedName("evaluatorPhone")
    @Expose
    private String evaluatorPhone;
    @SerializedName("evaluateePhone")
    @Expose
    private String evaluateePhone;
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

    public Integer getSelfId() {
        return selfId;
    }

    public void setSelfId(Integer selfId) {
        this.selfId = selfId;
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
