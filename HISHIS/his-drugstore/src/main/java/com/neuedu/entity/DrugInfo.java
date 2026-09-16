package com.neuedu.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DrugInfo {
    private Integer id;
    private String drugCode;
    private String drugName;
    private String drugFormat;
    private String drugUnit;
    private String manufacturer;
    private String drugDosage;
    private String drugType;
    private BigDecimal drugPrice;
    private String mnemonicCode;
    private Date creationDate;
    private Integer delmark;
    private Integer drugStock;

    //无参、全参构造
    public DrugInfo(){}

    //get set 全部生成
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDrugCode() { return drugCode; }
    public void setDrugCode(String drugCode) { this.drugCode = drugCode; }
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    public String getDrugFormat() { return drugFormat; }
    public void setDrugFormat(String drugFormat) { this.drugFormat = drugFormat; }
    public String getDrugUnit() { return drugUnit; }
    public void setDrugUnit(String drugUnit) { this.drugUnit = drugUnit; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getDrugDosage() { return drugDosage; }
    public void setDrugDosage(String drugDosage) { this.drugDosage = drugDosage; }
    public String getDrugType() { return drugType; }
    public void setDrugType(String drugType) { this.drugType = drugType; }
    public BigDecimal getDrugPrice() { return drugPrice; }
    public void setDrugPrice(BigDecimal drugPrice) { this.drugPrice = drugPrice; }
    public String getMnemonicCode() { return mnemonicCode; }
    public void setMnemonicCode(String mnemonicCode) { this.mnemonicCode = mnemonicCode; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Integer getDelmark() { return delmark; }
    public void setDelmark(Integer delmark) { this.delmark = delmark; }
    public Integer getDrugStock() { return drugStock; }
    public void setDrugStock(Integer drugStock) { this.drugStock = drugStock; }
}
