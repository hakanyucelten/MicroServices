package com.example.myapp;

import com.example.myapp.Methods.AuthenticationService;
import com.example.myapp.Methods.PasswordHashingService;
import com.example.myapp.Methods.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@RestController
public class HashingController {

    private final WeatherService weatherService;

    private final AuthenticationService authenticationService;

    @Autowired
    public HashingController(WeatherService weatherService, AuthenticationService authenticationService) {
        this.weatherService = weatherService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/hashPassword")
    public String hashPassword(@RequestBody Credentials credentials) {
        String hashedPassword = PasswordHashingService.hashPassword(credentials.getUsername(), credentials.getPassword());

        return "Hashed password for user " + credentials.getUsername() + ": " + hashedPassword;
    }

    @GetMapping("/weather")
    public ResponseEntity<String> getWeather(@RequestParam String city) {
        // Check for token in the request headers
        String token = getRequestToken();
        if (token != null && token.startsWith("Bearer ")) {
            // Extract the actual token value
            token = token.substring(7);

            // Call the getWeather method from WeatherService
            return weatherService.getWeather(city, token);
        } else {
            // Return 401 Unauthorized if token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. Please provide a valid token. Your token is this:" + token);
        }
    }
    private String getRequestToken() {
        // Retrieve the current HTTP request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Extract the token from the request headers
        String token = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer "
        if (token != null && token.startsWith("Bearer ")) {
            // Remove "Bearer " prefix to get the actual token value
            return token.substring(7);
        } else {
            return null; // Token is missing or in an invalid format
        }
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Credentials request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // Authenticate the user and generate a token
        String token = authenticationService.authenticate(username, password);

        if (token != null) {
            return "Authentication successful. Token: " + token;
        } else {
            return "Authentication failed. Invalid username or password.";
        }
    }
}
