package sample.domain.forum;

import lombok.NonNull;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author ykpark@woowahan.com
 */
@Embeddable
public class Password {

    @Column(name = "password")
    private String encodedPassword;

    private Password(@NonNull String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public boolean matches(String rawPassword) {
        return PasswordHelper.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Password password = (Password) o;

        return encodedPassword.equals(password.encodedPassword);

    }

    @Override
    public int hashCode() {
        return encodedPassword.hashCode();
    }

    @Override
    public String toString() {
        return encodedPassword;
    }

    // for hibernate
    private Password() { }


    public static Password wrap(CharSequence rawPassword) {
        return new Password(PasswordHelper.encode(rawPassword));
    }

    public static Password empty() {
        return new Password("");
    }


    interface PasswordProtectable {

        Password getPassword();

        default void verify(String rawPassword) {
            if (!getPassword().matches(rawPassword)) throw new ForumExceptions.BadPasswordException();
        }

    }

    static class PasswordHelper {

        static PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

        static String encode(CharSequence rawPassword) {
            return passwordEncoder.encode(rawPassword);
        }

        static boolean matches(CharSequence rawPassword, String encodedPassword) {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        }

        static void setPasswordEncoder(PasswordEncoder passwordEncoder) {
            PasswordHelper.passwordEncoder = passwordEncoder;
        }

    }

}
