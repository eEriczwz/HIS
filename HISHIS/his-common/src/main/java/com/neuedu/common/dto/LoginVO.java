package com.neuedu.common.dto;

import lombok.Data;

/**
 * 登录结果：token + 登录员工身份信息
 */
@Data
public class LoginVO {
    private String token;
    private Integer id;
    private String realname;
    private Integer deptmentId;
    private Integer registLevelId;
}
