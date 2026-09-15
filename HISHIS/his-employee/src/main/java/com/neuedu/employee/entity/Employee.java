package com.neuedu.employee.entity;

import lombok.Data;

@Data
public class Employee {
    private Integer id;
    private String realname;
    private String password;
    private Integer deptmentId;
    private Integer registLevelId;
    private Integer schedulingId;
    private Integer delmark;
}
