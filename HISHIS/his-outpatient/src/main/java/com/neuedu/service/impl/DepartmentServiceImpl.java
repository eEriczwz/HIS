package com.neuedu.service.impl;

import com.neuedu.entity.Department;
import com.neuedu.mapper.DepartmentMapper;
import com.neuedu.service.DepartmentService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> list() {
        // 查询未删除科室 delmark=1
        return departmentMapper.selectAll(1);
    }

    @Override
    public int add(Department department) {
        return departmentMapper.insert(department);
    }

    @Override
    public int removeById(Integer id) {
        return departmentMapper.deleteById(id);
    }

}
