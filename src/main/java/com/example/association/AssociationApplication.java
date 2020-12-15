package com.example.association;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@MapperScan("com.example.association.dao")
public class AssociationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssociationApplication.class, args);
    }

}
