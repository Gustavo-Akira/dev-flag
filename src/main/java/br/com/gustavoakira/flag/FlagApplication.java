package br.com.gustavoakira.flag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FlagApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlagApplication.class, args);
  }
}
