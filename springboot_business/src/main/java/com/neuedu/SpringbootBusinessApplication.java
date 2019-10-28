package com.neuedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
@MapperScan("com.neuedu.dao")
public class SpringbootBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBusinessApplication.class, args);
    }

}
