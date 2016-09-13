package com.woowahan.sample.modules.forum;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author ykpark@woowahan.com
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ForumConfig.class)
@Slf4j
public class PasswordHelperTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void passwordEncoderInjection() {
        assertThat(Password.PasswordHelper.passwordEncoder, is(passwordEncoder));
    }

    @Test
    public void printEncodedPassword() {
        log.info("password -> {}", Password.wrap("password"));
    }

}