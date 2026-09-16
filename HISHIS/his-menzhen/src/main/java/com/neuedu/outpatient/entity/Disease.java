package com.neuedu.outpatient.entity;

public class Disease {
    private Integer id;
    private String diseaseCode;
    private String diseaseName;
    private String diseaseICD;
    private String diseaseCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public void setDiseaseCode(String diseaseCode) {
        this.diseaseCode = diseaseCode;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseICD() {
        return diseaseICD;
    }

    public void setDiseaseICD(String diseaseICD) {
        this.diseaseICD = diseaseICD;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }
}
