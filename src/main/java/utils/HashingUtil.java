package utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

@Slf4j
public class HashingUtil {
    @Getter
    private static final String HASH_ALGORITHM;

    static {
        try {
            // Load the properties file
            InputStream propertiesStream = HashingUtil.class.getClassLoader().getResourceAsStream("hashing.properties");
            Properties prop = new Properties();
            prop.load(propertiesStream);

            // Retrieve the hash algorithm from the properties file
            HASH_ALGORITHM = prop.getProperty("hashAlgorithm");
        } catch (IOException e) {
            log.error("Error loading hashing.properties", e);
            throw new RuntimeException(e);
        }
    }

    private HashingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error hashing password with salt", e);
            throw new RuntimeException(e);
        }
    }

}
