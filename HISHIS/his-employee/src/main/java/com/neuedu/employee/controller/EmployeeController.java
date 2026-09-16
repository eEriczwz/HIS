package com.neuedu.employee.controller;

import com.neuedu.common.Result;
import com.neuedu.common.dto.LoginDTO;
import com.neuedu.common.dto.LoginVO;
import com.neuedu.employee.entity.Employee;
import com.neuedu.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO){
        return employeeService.login(loginDTO);
    }

    // 医生列表（医生工作站用于选择当前登录医生）
    @GetMapping("/list")
    public Result<List<Employee>> list(){
        return Result.success(employeeService.list());
    }
}

