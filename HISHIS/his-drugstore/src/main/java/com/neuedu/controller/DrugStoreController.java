package com.neuedu.controller;

import com.neuedu.entity.DrugInfo;
import com.neuedu.service.DrugInfoService;
import com.neuedu.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drugstore/drug")
public class DrugStoreController {

    @Resource
    private DrugInfoService drugInfoService;

    // 查询药品列表，支持药品编码、名称模糊查询
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String drugCode,
                       @RequestParam(required = false) String drugName){
        List<DrugInfo> list = drugInfoService.list(drugCode,drugName);
        return Result.success(list);
    }

    //新增药品
    @PostMapping("/add")
    public Result add(@RequestBody DrugInfo drugInfo){
        int rows = drugInfoService.add(drugInfo);
        if(rows>0){
            return Result.success("新增药品成功");
        }else {
            return Result.error("新增药品失败");
        }
    }

    //修改药品
    @PutMapping("/update")
    public Result update(@RequestBody DrugInfo drugInfo){
        int rows = drugInfoService.update(drugInfo);
        if(rows>0){
            return Result.success("修改药品成功");
        }else {
            return Result.error("修改药品失败");
        }
    }

    //逻辑删除药品
    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id){
        int rows = drugInfoService.deleteById(id);
        if(rows>0){
            return Result.success("删除药品成功");
        }else {
            return Result.error("删除药品失败");
        }
    }
}
