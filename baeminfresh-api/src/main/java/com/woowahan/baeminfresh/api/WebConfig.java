package com.woowahan.baeminfresh.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.woowahan.baeminfresh.api.support.jackson2.HttpStatusSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.SimpleDateFormat;

/**
 * @author ykpark@woowahan.com
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private SpringValidatorAdapter validator;

    public WebConfig(SpringValidatorAdapter validator) {
        this.validator = validator;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true)
                  .favorParameter(false)
                  .favorPathExtension(false)
                  .defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Autowired
    public void setUpObjectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule("baeminfresh");
        module.addSerializer(HttpStatus.class, new HttpStatusSerializer());

        objectMapper.registerModule(module);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Validator getValidator() {
        return validator;
    }

}
