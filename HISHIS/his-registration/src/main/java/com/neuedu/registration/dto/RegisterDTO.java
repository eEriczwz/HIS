package com.neuedu.registration.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class RegisterDTO {
    private String realName;
    private String gender;
    private String cardNumber;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private Integer age;
    private String ageType;
    private String homeAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date visitDate;
    private String noon;
    private Integer deptmentId;
    private Integer employeeId;
    private Integer registLevelId;
    private Integer settleCategoryId;
    private String isBook;
    private String registMethod;
    private BigDecimal registMoney;
}
