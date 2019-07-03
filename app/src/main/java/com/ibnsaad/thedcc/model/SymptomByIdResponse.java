package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SymptomByIdResponse implements Serializable {

    @SerializedName("symptomId")
    @Expose
    private Integer symptomId;
    @SerializedName("symptomName")
    @Expose
    private String symptomName;

    public Integer getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(Integer symptomId) {
        this.symptomId = symptomId;
    }

    public String getSymptomName() {
        return symptomName;
    }

    public void setSymptomName(String symptomName) {
        this.symptomName = symptomName;
    }

    @Override
    public String toString() {
        return "SymptomByIdResponse{" +
                "symptomId=" + symptomId +
                ", symptomName='" + symptomName + '\'' +
                '}';
    }
}