package org.example.data;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDataFactory {

    public static String uniqueEmail(){
        return "qa." + UUID.randomUUID() + "@example.com";
    }

    public static Map<String, Object> userBody(
            String name, String email, String gender, String status){

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("gender", gender);
        body.put("status", status);
        return body;
    }

    public static String nameOfLength(int length) {
        return "A".repeat(length);
    }
}
