package org.example.tests;

import org.example.clients.UsersClient;
import org.example.data.UserDataFactory;
import org.testng.annotations.Test;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;

public class AuthenticationTest {
    @Test(groups = {"smoke", "regression"})
    public void invalidTokenIsRejected(){
        String uniqueEmail = UserDataFactory.uniqueEmail();
        Map<String, Object> requestBody = UserDataFactory.userBody("Sidd", uniqueEmail, "male", "active");
        UsersClient usersClient = new UsersClient();
        usersClient.createUser("Invalid Token", requestBody)
                .then().log().ifValidationFails()
                .statusCode(401)
                .body("message", equalTo("Invalid token"));
    }

    @Test(groups = "regression")
    public void missingTokenIsRejected(){
        String uniqueEmail = UserDataFactory.uniqueEmail();
        Map<String, Object> requestBody = UserDataFactory.userBody("Sidd", uniqueEmail, "male", "active");
        UsersClient usersClient = new UsersClient();
        usersClient.createUserWithoutToken(requestBody)
                .then().log().ifValidationFails()
                .statusCode(401)
                .body("message", equalTo("Authentication failed"));
    }
}
