package org.example.clients;

import io.restassured.response.Response;
import org.example.config.ApiConfig;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.example.config.ApiConfig.BASE_URL;

public class UsersClient {
    private static final String USERS_PATH = "/public/v2/users";
    public Response createUser(String token, Map<String, Object> body){
        return given()
                    .spec(ApiConfig.authenticationRequest(token))
                    .contentType("application/json")
                    .body(body)
                .when()
                    .post(USERS_PATH);
    }

    public Response getUser(String token, int userId){
        return given()
                    .spec(ApiConfig.authenticationRequest(token))
                    .pathParams("userId", userId)
                .when()
                    .get(USERS_PATH+"/{userId}");
    }

    public Response updateUser(String token, int userId, Map<String, Object> body){
        return given()
                    .spec(ApiConfig.authenticationRequest(token))
                    .contentType("application/json")
                    .pathParams("userId", userId)
                    .body(body)
                .when()
                    .patch(USERS_PATH+"/{userId}");
    }

    public Response deleteUser(String token, int userId){
        return given()
                    .spec(ApiConfig.authenticationRequest(token))
                    .pathParams("userId", userId)
                .when()
                    .delete(USERS_PATH+"/{userId}");
    }

    public Response createUserWithoutToken(Map<String, Object> body) {
        return given()
                    .baseUri(BASE_URL)
                    .accept("application/json")
                    .contentType("application/json")
                    .body(body)
                .when()
                    .post(USERS_PATH);
    }

    public Response getUsers(Map<String, Object> queryParams) {
        return given()
                    .baseUri(BASE_URL)
                    .accept("application/json")
                    .queryParams(queryParams)
                .when()
                    .get(USERS_PATH);
    }

    public Response getPublicUser(int userId) {
        return given()
                    .baseUri(BASE_URL)
                    .accept("application/json")
                    .pathParam("userId", userId)
                .when()
                    .get(USERS_PATH+"/{userId}");
    }
}
