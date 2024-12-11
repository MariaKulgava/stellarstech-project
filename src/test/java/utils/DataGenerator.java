package utils;

import java.util.UUID;

public class DataGenerator {
    public static String generateRandomUsername() {
        return "User_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static int generateRandomId() {
        return (int) (Math.random() * 1_000_000);
    }
}
