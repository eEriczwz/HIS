package com.neuedu.controller;

import com.neuedu.common.Result;
import com.neuedu.entity.Prescription;
import com.neuedu.service.DispenseService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/drugstore/dispense")
public class DispenseController {

    @Resource
    private DispenseService dispenseService;

    // 发药列表：state 传 pending 表示待发药（已开立/已缴费）
    @GetMapping("/list")
    public Result<List<Prescription>> list(@RequestParam(required = false) String state,
                                           @RequestParam(required = false) String caseNumber,
                                           @RequestParam(required = false) String drugName){
        return Result.success(dispenseService.list(state, caseNumber, drugName));
    }

    // 发药：扣减库存 + 状态置为已发药
    @PutMapping("/issue/{id}")
    public Result issue(@PathVariable Integer id){
        int rows = dispenseService.issue(id);
        if (rows < 0) {
            return Result.error("库存不足，无法发药");
        }
        return rows > 0 ? Result.success("发药成功") : Result.error("发药失败（仅已开立/已缴费的处方可发药）");
    }

    // 退药：状态置为已退药
    @PutMapping("/return/{id}")
    public Result returnDrug(@PathVariable Integer id){
        int rows = dispenseService.returnDrug(id);
        return rows > 0 ? Result.success("退药成功") : Result.error("退药失败（仅已发药处方可退药）");
    }
}
