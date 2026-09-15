package com.neuedu.employee.mapper;

import com.neuedu.employee.entity.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {
    Employee selectByRealname(@Param("realname") String realname);
}
