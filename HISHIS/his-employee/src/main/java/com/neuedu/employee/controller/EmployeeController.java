package com.neuedu.employee.controller;

import com.neuedu.common.Result;
import com.neuedu.common.dto.LoginDTO;
import com.neuedu.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        return employeeService.login(loginDTO);
    }
}

