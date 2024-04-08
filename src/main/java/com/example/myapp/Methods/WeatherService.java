package com.example.myapp.Methods;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class WeatherService {

    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "apikey"; // Replace with your actual API key
    private static final Set<String> AUTHORIZED_TOKENS = new HashSet<>(Arrays.asList(
            "SGFrYW46c2VjcmV0S2V5XzY5NDg5MTgz",
            "SGFydW46c2VjcmV0S2V5XzY5NDk2NTMw",
            "SGFydW46c2VjcmV0S2V5XzY5NDk2NTyp",
            "QWhtZXQ6c2VjcmV0S2V5XzYzMjM1MTI1"
    ));

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getWeather(String city, String token) {
        if (isValidToken(token)) {
            // Create headers with the token
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            // Set up the API URL with query parameters
            String apiUrl = WEATHER_API_URL + "?q=" + city + "&appid=" + API_KEY;

            try {
                // Make the request with headers
                return restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            } catch (HttpClientErrorException e) {
                // Handle errors (e.g., unauthorized access)
                return ResponseEntity.status(e.getRawStatusCode()).body("Error accessing weather data: " + e.getMessage());
            }
        } else {
            // Return 401 Unauthorized if token is missing or invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. Please provide a valid token.");
        }
    }

    private boolean isValidToken(String token) {
        // Check if the token matches any of the authorized tokens
        return token != null && AUTHORIZED_TOKENS.contains(token);
    }
}
