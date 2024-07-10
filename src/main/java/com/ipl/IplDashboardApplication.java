package com.ipl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = { "com.ipl.data", "com.ipl.model","com.ipl.controller","com.ipl.exception","com.ipl.repository" })
public class IplDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(IplDashboardApplication.class, args);
	}

}
