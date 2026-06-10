package com.rabbiter.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabbiter.hospital.pojo.Doctor;

import java.util.ArrayList;
import java.util.List;

public class GuidanceResult {
    @JsonProperty("recommendedDepartment")
    private String recommendedDepartment;
    @JsonProperty("diseaseName")
    private String diseaseName;
    private String advice;
    private String location;
    @JsonProperty("routeTip")
    private String routeTip;
    @JsonProperty("matchedKeywords")
    private List<String> matchedKeywords = new ArrayList<>();
    private Integer score;
    private List<Doctor> doctors = new ArrayList<>();

    public String getRecommendedDepartment() {
        return recommendedDepartment;
    }

    public void setRecommendedDepartment(String recommendedDepartment) {
        this.recommendedDepartment = recommendedDepartment;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRouteTip() {
        return routeTip;
    }

    public void setRouteTip(String routeTip) {
        this.routeTip = routeTip;
    }

    public List<String> getMatchedKeywords() {
        return matchedKeywords;
    }

    public void setMatchedKeywords(List<String> matchedKeywords) {
        this.matchedKeywords = matchedKeywords;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
