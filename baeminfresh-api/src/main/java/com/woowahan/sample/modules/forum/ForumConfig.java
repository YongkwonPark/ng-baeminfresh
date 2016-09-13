package com.woowahan.sample.modules.forum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ykpark@woowahan.com
 */
@Configuration
public class ForumConfig {

    @Bean
    public PasswordEncoder forumPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Password.PasswordHelper passwordHelper(PasswordEncoder forumPasswordEncoder) {
        Password.PasswordHelper passwordHelper = new Password.PasswordHelper();
        passwordHelper.setPasswordEncoder(forumPasswordEncoder);

        return passwordHelper;
    }

}
