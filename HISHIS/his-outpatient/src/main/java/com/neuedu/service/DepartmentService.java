package com.neuedu.service;

import com.neuedu.entity.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> list();
    // 新增部门
    int add(Department department);

    // 逻辑删除
    int removeById(Integer id);

}
