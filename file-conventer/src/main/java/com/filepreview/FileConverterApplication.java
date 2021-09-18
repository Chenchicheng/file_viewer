package com.filepreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileConverterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileConverterApplication.class, args);
	}
}
