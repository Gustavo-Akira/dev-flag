package br.com.gustavoakira.flag;

import org.springframework.boot.SpringApplication;

public class TestFlagApplication {

	public static void main(String[] args) {
		SpringApplication.from(FlagApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
