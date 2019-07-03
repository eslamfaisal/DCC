package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TokenResponse implements Serializable {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("exception")
    @Expose
    private Object exception;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("isCanceled")
    @Expose
    private Boolean isCanceled;
    @SerializedName("isCompleted")
    @Expose
    private Boolean isCompleted;
    @SerializedName("isCompletedSuccessfully")
    @Expose
    private Boolean isCompletedSuccessfully;
    @SerializedName("creationOptions")
    @Expose
    private Integer creationOptions;
    @SerializedName("asyncState")
    @Expose
    private Object asyncState;
    @SerializedName("isFaulted")
    @Expose
    private Boolean isFaulted;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Boolean getIsCompletedSuccessfully() {
        return isCompletedSuccessfully;
    }

    public void setIsCompletedSuccessfully(Boolean isCompletedSuccessfully) {
        this.isCompletedSuccessfully = isCompletedSuccessfully;
    }

    public Integer getCreationOptions() {
        return creationOptions;
    }

    public void setCreationOptions(Integer creationOptions) {
        this.creationOptions = creationOptions;
    }

    public Object getAsyncState() {
        return asyncState;
    }

    public void setAsyncState(Object asyncState) {
        this.asyncState = asyncState;
    }

    public Boolean getIsFaulted() {
        return isFaulted;
    }

    public void setIsFaulted(Boolean isFaulted) {
        this.isFaulted = isFaulted;
    }

}