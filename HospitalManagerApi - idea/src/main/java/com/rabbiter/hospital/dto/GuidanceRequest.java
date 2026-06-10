package com.rabbiter.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuidanceRequest {
    @JsonProperty("symptomText")
    private String symptomText;
    @JsonProperty("patientId")
    private Integer patientId;

    public String getSymptomText() {
        return symptomText;
    }

    public void setSymptomText(String symptomText) {
        this.symptomText = symptomText;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
