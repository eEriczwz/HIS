package com.neuedu.outpatient.entity;

import java.util.Date;
import java.math.BigDecimal;

public class CheckRequest {
    private Integer id;
    private Integer registerId;
    private Integer medicalTechnologyId;
    private String checkInfo;
    private String checkPosition;
    private Date creationTime;
    private Integer checkEmployeeId;
    private Integer inputcheckEmployeeId;
    private Date checkTime;
    private String checkResult;
    private String checkState;
    private String checkRemark;
    // 联查展示字段（非表字段）：医技项目名称、单价
    private String techName;
    private BigDecimal techPrice;

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

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public String getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(String checkPosition) {
        this.checkPosition = checkPosition;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCheckEmployeeId() {
        return checkEmployeeId;
    }

    public void setCheckEmployeeId(Integer checkEmployeeId) {
        this.checkEmployeeId = checkEmployeeId;
    }

    public Integer getInputcheckEmployeeId() {
        return inputcheckEmployeeId;
    }

    public void setInputcheckEmployeeId(Integer inputcheckEmployeeId) {
        this.inputcheckEmployeeId = inputcheckEmployeeId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public BigDecimal getTechPrice() {
        return techPrice;
    }

    public void setTechPrice(BigDecimal techPrice) {
        this.techPrice = techPrice;
    }
}
