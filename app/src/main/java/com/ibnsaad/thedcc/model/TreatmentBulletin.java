package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TreatmentBulletin implements Serializable {

    @SerializedName("treatmentBulletinId")
    @Expose
    private Integer treatmentBulletinId;
    @SerializedName("drugId")
    @Expose
    private Integer drugId;
    @SerializedName("composition")
    @Expose
    private String composition;
    @SerializedName("indications")
    @Expose
    private String indications;
    @SerializedName("dosing")
    @Expose
    private String dosing;
    @SerializedName("sideEffects")
    @Expose
    private String sideEffects;

    public Integer getTreatmentBulletinId() {
        return treatmentBulletinId;
    }

    public void setTreatmentBulletinId(Integer treatmentBulletinId) {
        this.treatmentBulletinId = treatmentBulletinId;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getDosing() {
        return dosing;
    }

    public void setDosing(String dosing) {
        this.dosing = dosing;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    @Override
    public String toString() {
        return "TreatmentBulletin{" +
                "treatmentBulletinId=" + treatmentBulletinId +
                ", drugId=" + drugId +
                ", composition='" + composition + '\'' +
                ", indications='" + indications + '\'' +
                ", dosing='" + dosing + '\'' +
                ", sideEffects='" + sideEffects + '\'' +
                '}';
    }
}