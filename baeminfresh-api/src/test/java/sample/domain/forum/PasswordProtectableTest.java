package sample.domain.forum;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ykpark@woowahan.com
 */
public class PasswordProtectableTest {

    @Test
    public void verify() {
        String password = "password";
        Source source = new Source(password);

        try {
            source.verify("");
            fail();
        } catch (ForumExceptions.BadPasswordException e) {
            assertNotNull(e);
        }

        source.verify(password);
    }


    class Source implements Password.PasswordProtectable {

        final Password password;

        Source(String rawPassword) {
            this.password = Password.wrap(rawPassword);
        }

        @Override
        public Password getPassword() {
            return password;
        }

    }

}