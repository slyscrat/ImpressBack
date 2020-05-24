package com.slyscrat.impress.service.security;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordGeneratorService {
    private final PasswordEncoder encoder;

    @Value("${security.password-len}")
    private Integer passwordLength;

    @Value("${security.use-letters}")
    private boolean useLetters;

    @Value("${security.use-numbers}")
    private boolean useNumbers;

    public String generateEncryptedRandomPassword() {
        return encoder.encode(generateRandomPassword());
    }

    public String encryptPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }

    public String generateRandomPassword() {
        return RandomStringUtils.random(passwordLength, useLetters, useNumbers);
    }
}
