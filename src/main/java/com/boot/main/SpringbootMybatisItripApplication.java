package com.boot.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.boot.mapper"})
@ComponentScan(basePackages = {"com.boot.po","com.boot.service","com.boot.controller"})
public class SpringbootMybatisItripApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisItripApplication.class, args);
    }

}
