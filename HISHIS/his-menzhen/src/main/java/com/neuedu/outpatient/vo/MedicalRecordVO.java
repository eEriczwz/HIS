package com.neuedu.outpatient.vo;

import com.neuedu.outpatient.entity.*;
import java.util.List;

public class MedicalRecordVO {
    private Register register;          //挂号信息
    private MedicalRecord medicalRecord; //病历
    private List<Disease> diseaseList;  //诊断疾病列表
    private List<CheckRequest> checkList;   //检查单
    private List<DisposalRequest> disposalList; //处置单
    private List<Prescription> prescriptionList; //处方

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public List<CheckRequest> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckRequest> checkList) {
        this.checkList = checkList;
    }

    public List<DisposalRequest> getDisposalList() {
        return disposalList;
    }

    public void setDisposalList(List<DisposalRequest> disposalList) {
        this.disposalList = disposalList;
    }

    public List<Prescription> getPrescriptionList() {
        return prescriptionList;
    }

    public void setPrescriptionList(List<Prescription> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }
}
