package com.neuedu.mapper;

import com.neuedu.entity.Department;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(@Param("delmark") Integer delmark);
    // 新增部门
    int insert(Department department);

    // 逻辑删除：根据id，把delmark更新为0
    int deleteById(Integer id);

}
