package com.example.myapp.Methods;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class AuthenticationService {

    // Map to store valid username-password pairs
    private static final Map<String, String> VALID_CREDENTIALS = new HashMap<>();

    static {
        VALID_CREDENTIALS.put("Hakan", "1903");
        VALID_CREDENTIALS.put("Harun", "1234");
        VALID_CREDENTIALS.put("Ahmet", "1905");
    }
    private final RestTemplate restTemplate;
    public AuthenticationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authenticate(String username, String password) {
        // Verify if the provided username and password are valid
        if (VALID_CREDENTIALS.containsKey(username) && VALID_CREDENTIALS.get(username).equals(password)) {
            // Generate token using Base64 encoding
            String token = generateToken(username);
            return token;
        } else {
            // Return null if the provided credentials are invalid
            return null;
        }
    }

    private String generateToken(String username) {
        // Concatenate username with a unique secret key for each user
        String uniqueSecretKey = getUniqueSecretKey(username);
        String tokenData = username + ":" + uniqueSecretKey;
        // Encode the token data using Base64 encoding
        String encodedToken = Base64.getEncoder().encodeToString(tokenData.getBytes());
        return encodedToken;
    }

    private String getUniqueSecretKey(String username) {
        // Example: You can use a hash function to generate a unique secret key for each user
        return "secretKey_" + username.hashCode();
    }
}
