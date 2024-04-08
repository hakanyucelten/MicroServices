package com.example.myapp.Methods;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHashingService {

    private static final int ITERATIONS = 10;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 1;

    public static String hashPassword(String username, String password) {
        Argon2 argon2 = Argon2Factory.create();

        try {
            String combinedInput = username + ":" + password;
            return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, combinedInput);
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}
