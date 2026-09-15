package com.neuedu.employee.service.impl;

import com.neuedu.common.Result;
import com.neuedu.common.dto.LoginDTO;
import com.neuedu.employee.entity.Employee;
import com.neuedu.employee.mapper.EmployeeMapper;
import com.neuedu.employee.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Override
    public Result login(LoginDTO loginDTO) {
        log.info("收到登录请求，realname={}", loginDTO.getRealname());
        Employee employee = employeeMapper.selectByRealname(loginDTO.getRealname());
        log.info("mybatis查询结果 employee={}", employee);

        if(employee == null){
            return Result.error("用户名不存在");
        }

        if(!employee.getPassword().equals(loginDTO.getPassword())){
            return Result.error("密码错误");
        }
        String token = UUID.randomUUID().toString().replace("-","");
        return Result.success(token);
    }
}
