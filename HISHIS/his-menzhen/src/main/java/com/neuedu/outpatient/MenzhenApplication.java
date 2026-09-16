package com.neuedu.outpatient;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neuedu.outpatient.mapper") // 重点！
public class MenzhenApplication {
    public static void main(String[] args) {
        SpringApplication.run(MenzhenApplication.class, args);
    }
}

