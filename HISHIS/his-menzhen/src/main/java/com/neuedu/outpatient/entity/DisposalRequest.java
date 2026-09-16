package com.neuedu.outpatient.entity;

import java.util.Date;

public class DisposalRequest {
    private Integer id;
    private Integer registerId;
    private Integer medicalTechnologyId;
    private String disposalInfo;
    private String disposalPosition;
    private Date creationTime;
    private Integer disposalEmployeeId;
    private Integer inputdisposalEmployeeId;
    private Date disposalTime;
    private String disposalResult;
    private String disposalState;
    private String disposalRemark;
    // 联查展示字段（非表字段）：医技项目名称
    private String techName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public Integer getMedicalTechnologyId() {
        return medicalTechnologyId;
    }

    public void setMedicalTechnologyId(Integer medicalTechnologyId) {
        this.medicalTechnologyId = medicalTechnologyId;
    }

    public String getDisposalInfo() {
        return disposalInfo;
    }

    public void setDisposalInfo(String disposalInfo) {
        this.disposalInfo = disposalInfo;
    }

    public String getDisposalPosition() {
        return disposalPosition;
    }

    public void setDisposalPosition(String disposalPosition) {
        this.disposalPosition = disposalPosition;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getDisposalEmployeeId() {
        return disposalEmployeeId;
    }

    public void setDisposalEmployeeId(Integer disposalEmployeeId) {
        this.disposalEmployeeId = disposalEmployeeId;
    }

    public Integer getInputdisposalEmployeeId() {
        return inputdisposalEmployeeId;
    }

    public void setInputdisposalEmployeeId(Integer inputdisposalEmployeeId) {
        this.inputdisposalEmployeeId = inputdisposalEmployeeId;
    }

    public Date getDisposalTime() {
        return disposalTime;
    }

    public void setDisposalTime(Date disposalTime) {
        this.disposalTime = disposalTime;
    }

    public String getDisposalResult() {
        return disposalResult;
    }

    public void setDisposalResult(String disposalResult) {
        this.disposalResult = disposalResult;
    }

    public String getDisposalState() {
        return disposalState;
    }

    public void setDisposalState(String disposalState) {
        this.disposalState = disposalState;
    }

    public String getDisposalRemark() {
        return disposalRemark;
    }

    public void setDisposalRemark(String disposalRemark) {
        this.disposalRemark = disposalRemark;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }
}
