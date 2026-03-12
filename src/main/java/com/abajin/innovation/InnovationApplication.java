package com.abajin.innovation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.abajin.innovation")
public class InnovationApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovationApplication.class, args);
    }

}
