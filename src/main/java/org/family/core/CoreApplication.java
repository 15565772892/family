package org.family.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.family.core.dao")
@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
