package org.example.helpers;

import org.example.clients.UsersClient;
import static org.hamcrest.Matchers.equalTo;

public class UserDeleteHelper {

    public void deleteUser(int userId, String token){
        UsersClient usersClient = new UsersClient();
        usersClient.deleteUser(token, userId)
                .then().log().ifValidationFails()
                .statusCode(204);
    }

    public void verifyUserIsDeleted(int userId, String token){
        UsersClient usersClient = new UsersClient();
        usersClient.getUser(token, userId)
                .then().log().ifValidationFails()
                .statusCode(404)
                .body("message", equalTo("Resource not found"));
    }
}
