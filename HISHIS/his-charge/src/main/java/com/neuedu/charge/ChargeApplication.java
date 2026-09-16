package com.neuedu.charge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.neuedu.charge.mapper")
public class ChargeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChargeApplication.class, args);
    }
}
