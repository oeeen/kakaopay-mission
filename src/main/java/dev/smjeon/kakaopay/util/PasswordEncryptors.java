package dev.smjeon.kakaopay.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptors {

    public static String encrypt(String before) {
        return BCrypt.hashpw(before, BCrypt.gensalt());
    }
}
