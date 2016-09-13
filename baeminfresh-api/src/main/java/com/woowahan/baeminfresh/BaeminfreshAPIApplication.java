package com.woowahan.baeminfresh;

import com.woowahan.sample.SampleSystemConfig;
import com.woowahan.swagger.Swagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@Import({ Swagger.class, SampleSystemConfig.class })
public class BaeminfreshAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaeminfreshAPIApplication.class, args);
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
