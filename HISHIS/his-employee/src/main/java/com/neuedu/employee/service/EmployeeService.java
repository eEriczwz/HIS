package com.neuedu.employee.service;

import com.neuedu.common.Result;
import com.neuedu.common.dto.LoginDTO;

public interface EmployeeService {
    Result login(LoginDTO loginDTO);
}
