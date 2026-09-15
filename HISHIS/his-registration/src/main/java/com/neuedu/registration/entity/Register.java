package com.neuedu.registration.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Register {
    private Integer id;
    private String caseNumber;
    private String realName;
    private String gender;
    private String cardNumber;
    private Date birthday;
    private Integer age;
    private String ageType;
    private String homeAddress;
    private Date visitDate;
    private String noon;
    private Integer deptmentId;
    private Integer employeeId;
    private Integer registLevelId;
    private Integer settleCategoryId;
    private String isBook;
    private String registMethod;
    private Integer visitState;
    private BigDecimal registMoney;
}
