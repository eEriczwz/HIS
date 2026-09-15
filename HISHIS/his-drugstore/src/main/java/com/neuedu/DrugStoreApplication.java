package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.neuedu.mapper")
@SpringBootApplication
public class DrugStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(DrugStoreApplication.class, args);
    }
}
