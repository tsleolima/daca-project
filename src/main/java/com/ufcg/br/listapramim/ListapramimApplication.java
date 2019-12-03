package com.ufcg.br.listapramim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;

@SpringBootApplication
@EnableCaching
@EnableSqs
public class ListapramimApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListapramimApplication.class, args);
	}
	
}
