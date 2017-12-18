package com.bingo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan({"com.bingo.dao"})
public class TxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TxApplication.class, args);
	}
}
