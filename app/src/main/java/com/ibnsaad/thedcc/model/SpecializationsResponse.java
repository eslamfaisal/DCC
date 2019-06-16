package com.ibnsaad.thedcc.model;

public class SpecializationsResponse {
    private String specialize;

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    @Override
    public String toString() {
        return "SpecializationsResponse{" +
                "specialize='" + specialize + '\'' +
                '}';
    }
}
