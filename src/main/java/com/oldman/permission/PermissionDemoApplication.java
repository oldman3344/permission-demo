package com.oldman.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.oldman.permission.mapper")
@SpringBootApplication
public class PermissionDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionDemoApplication.class, args);
    }

}
