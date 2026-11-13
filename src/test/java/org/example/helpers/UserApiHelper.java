package org.example.helpers;

import org.example.clients.UsersClient;
import static org.hamcrest.Matchers.equalTo;

public class UserApiHelper {
    public void getUsersById(int userId, String token, String name, String email, String gender, String status){
        UsersClient usersClient = new UsersClient();
        usersClient.getUser(token, userId)
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("name", equalTo(name))
                .body("email", equalTo(email))
                .body("gender", equalTo(gender))
                .body("status", equalTo(status));
    }
}
