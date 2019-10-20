package com.ufcg.br.listapramim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ListapramimApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListapramimApplication.class, args);
	}

}
