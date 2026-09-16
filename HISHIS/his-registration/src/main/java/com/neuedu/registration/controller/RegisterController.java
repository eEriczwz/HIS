package com.neuedu.registration.controller;

import com.neuedu.common.Result;
import com.neuedu.registration.dto.RegisterDTO;
import com.neuedu.registration.dto.RegisterQueryDTO;
import com.neuedu.registration.entity.Register;
import com.neuedu.registration.service.RegisterService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Resource
    private RegisterService registerService;

    /**
     * 新建挂号
     */
    @PostMapping("/add")
    public Result<String> add(@RequestBody RegisterDTO dto){
        String caseNo = registerService.createRegister(dto);
        return Result.success("挂号成功，病历号："+caseNo,caseNo);
    }

    /**
     * 挂号列表查询
     */
    @GetMapping("/list")
    public Result<List<Register>> list(RegisterQueryDTO queryDTO){
        List<Register> list = registerService.getList(queryDTO);
        return Result.success(list);
    }

    /**
     * 挂号详情
     */
    @GetMapping("/{id}")
    public Result<Register> getOne(@PathVariable Integer id){
        Register register = registerService.getById(id);
        return Result.success(register);
    }

    /**
     * 修改就诊状态
     * 1已挂号，2医生接诊，3看诊结束，4已退号
     */
    @PutMapping("/state/{id}/{state}")
    public Result<?> updateState(@PathVariable Integer id,@PathVariable Integer state){
        boolean ok = registerService.changeState(id,state);
        if(ok){
            return Result.success("状态修改成功");
        }
        return Result.error("修改失败");
    }
}
