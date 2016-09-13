package com.woowahan.swagger;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Swagger를 위한 독립적인 DispatcherServlet 구성
 *
 * @author ykpark@woowahan.com
 */
@Configuration
public class Swagger {

    @Bean
    public ServletRegistrationBean swagger() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new SwaggerDispatcherServlet(), "/swagger/*");
        registrationBean.setName("swagger");

        return registrationBean;
    }


    class SwaggerDispatcherServlet extends DispatcherServlet {

        public SwaggerDispatcherServlet() {
            AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
            webApplicationContext.register(SwaggerUIConfig.class);

            setApplicationContext(webApplicationContext);
        }

        @Override
        protected WebApplicationContext initWebApplicationContext() {
            WebApplicationContext wac = getWebApplicationContext();
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                if (!cwac.isActive()) {
                    configureAndRefreshWebApplicationContext(cwac);
                }
            }

            return wac;
        }

    }

}
