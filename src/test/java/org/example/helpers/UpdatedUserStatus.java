package org.example.helpers;

import org.example.clients.UsersClient;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;

public class UpdatedUserStatus {

    public void updateStatus(int userId, String token, String updatedStatus){
        UsersClient usersClient = new UsersClient();
        usersClient.updateUser(token, userId, Map.of("status", updatedStatus))
                .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body("id", equalTo(userId))
                    .body("status", equalTo(updatedStatus));
    }
}
