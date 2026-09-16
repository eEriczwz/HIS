package com.neuedu.employee.service;

import com.neuedu.common.Result;
import com.neuedu.common.dto.LoginDTO;
import com.neuedu.common.dto.LoginVO;
import com.neuedu.employee.entity.Employee;
import java.util.List;

public interface EmployeeService {
    Result<LoginVO> login(LoginDTO loginDTO);
    List<Employee> list();
}
