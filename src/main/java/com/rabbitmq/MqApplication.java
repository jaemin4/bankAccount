package com.rabbitmq;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;


@Profile("rabbitmq")
@SpringBootApplication
public class MqApplication {


	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(MqApplication.class, args);
	}


}
