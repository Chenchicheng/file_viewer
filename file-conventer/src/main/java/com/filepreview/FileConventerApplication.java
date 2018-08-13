package com.filepreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileConventerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConventerApplication.class, args);
	}
}
