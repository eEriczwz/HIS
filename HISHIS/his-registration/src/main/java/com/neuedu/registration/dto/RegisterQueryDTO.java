package com.neuedu.registration.dto;

import lombok.Data;
import java.util.Date;

@Data
public class RegisterQueryDTO {
    private String realName;
    private Integer deptmentId;
    private Integer visitState;
    private Date visitDate;
}
