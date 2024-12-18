package com.khineMyanmar;

import org.springframework.boot.SpringApplication;

public class TestKhineMyanmarApplication {

	public static void main(String[] args) {
		SpringApplication.from(KhineMyanmarApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
