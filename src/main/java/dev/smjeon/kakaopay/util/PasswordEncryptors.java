package dev.smjeon.kakaopay.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptors {

    public String encrypt(String before) {
        return BCrypt.hashpw(before, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }
}
