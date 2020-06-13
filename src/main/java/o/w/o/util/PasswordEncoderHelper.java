package o.w.o.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderHelper {
    public static BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
