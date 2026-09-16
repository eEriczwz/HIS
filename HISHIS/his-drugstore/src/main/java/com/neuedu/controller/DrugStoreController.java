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

    //入库：增加库存
    @PutMapping("/stockIn/{id}/{qty}")
    public Result stockIn(@PathVariable Integer id, @PathVariable Integer qty){
        int rows = drugInfoService.stockIn(id, qty);
        return rows>0 ? Result.success("入库成功") : Result.error("入库失败");
    }

    //出库：减少库存（库存不足返回错误）
    @PutMapping("/stockOut/{id}/{qty}")
    public Result stockOut(@PathVariable Integer id, @PathVariable Integer qty){
        int rows = drugInfoService.stockOut(id, qty);
        return rows>0 ? Result.success("出库成功") : Result.error("库存不足或出库失败");
    }
}
