package mediHub_be;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12345"; // 기존 비밀번호
        String encodedPassword = encoder.encode(rawPassword); // 암호화된 비밀번호
        System.out.println("Encoded Password: " + encodedPassword);
    }
}

