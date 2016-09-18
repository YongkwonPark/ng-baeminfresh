package com.woowahan;

import sample.SampleSystemConfig;
import swagger.Swagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@Import({ Swagger.class, SampleSystemConfig.class })
public class BaeminfreshApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaeminfreshApiApplication.class, args);
	}

	@Bean
    public ErrorProperties errorProperties() {
	    return new ErrorProperties();
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

}
