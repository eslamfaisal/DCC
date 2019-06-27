package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrugsResponse {

    @SerializedName("drugId")
    @Expose
    private Integer drugId;
    @SerializedName("drugName")
    @Expose
    private String drugName;
    @SerializedName("treatmentBulletin")
    @Expose
    private TreatmentBulletin treatmentBulletin;

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public TreatmentBulletin getTreatmentBulletin() {
        return treatmentBulletin;
    }

    public void setTreatmentBulletin(TreatmentBulletin treatmentBulletin) {
        this.treatmentBulletin = treatmentBulletin;
    }

    @Override
    public String toString() {
        return "DrugsResponse{" +
                "drugId=" + drugId +
                ", drugName='" + drugName + '\'' +
                ", treatmentBulletin=" + treatmentBulletin +
                '}';
    }
}