package org.example.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Properties CONFIG = loadProperties();
    public static final String BASE_URL = System.getProperty("baseUrl", CONFIG.getProperty("baseUrl"));
    public static RequestSpecification authenticationRequest(String token){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setAccept("application/json")
                .addHeader("Authorization", "Bearer "+token)
                .build();
    }

    public static String token(){
        String token = System.getenv("GOREST_TOKEN");
        if(token == null || token.isBlank()){
            throw new IllegalStateException("Set token in the IntelliJ run configuration");
        }
        return token;
    }

    public static Properties loadProperties(){
        Properties properties = new Properties();
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream("config.properties")){
            if(input == null){
                throw new IllegalStateException("config.properties was not found");
            }
            properties.load(input);
            return properties;
        }catch (IOException e){
            throw new IllegalStateException(
                    "Could not read config.properties", e
            );
        }
    }
}
