package com.family.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.family.order.mapper")
public class FamilyOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyOrderApplication.class, args);
    }
}