package com.neuedu.controller;

import com.neuedu.common.Result;
import com.neuedu.entity.Department;
import com.neuedu.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/list")
    public Result<List<Department>> list(){
        List<Department> list = departmentService.list();
        return Result.success(list);
    }

    /**
     * 新增部门
     * @param department 部门实体
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department){
        int row = departmentService.add(department);
        if(row > 0){
            return Result.success("新增部门成功");
        }
        return Result.error("新增部门失败");
    }

    /**
     * 逻辑删除部门
     * @param id 部门id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id){
        int row = departmentService.removeById(id);
        if(row > 0){
            return Result.success("删除部门成功");
        }
        return Result.error("删除部门失败");
    }
}
