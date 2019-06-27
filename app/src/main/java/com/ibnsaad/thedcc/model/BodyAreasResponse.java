package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyAreasResponse {

    @SerializedName("bodyAreasId")
    @Expose
    private Integer bodyAreasId;
    @SerializedName("nameArea")
    @Expose
    private String nameArea;
    @SerializedName("symptoms")
    @Expose
    private Object symptoms;

    public Integer getBodyAreasId() {
        return bodyAreasId;
    }

    public void setBodyAreasId(Integer bodyAreasId) {
        this.bodyAreasId = bodyAreasId;
    }

    public String getNameArea() {
        return nameArea;
    }

    public void setNameArea(String nameArea) {
        this.nameArea = nameArea;
    }

    public Object getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Object symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public String toString() {
        return "BodyAreasResponse{" +
                "bodyAreasId=" + bodyAreasId +
                ", nameArea='" + nameArea + '\'' +
                ", symptoms=" + symptoms +
                '}';
    }
}
