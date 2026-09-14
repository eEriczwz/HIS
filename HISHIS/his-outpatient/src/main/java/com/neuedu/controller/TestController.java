package com.neuedu.controller;

import com.neuedu.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public Result<String> hello(){
        return Result.success("✅ his‑outpatient门诊模块运行成功");
    }
}
