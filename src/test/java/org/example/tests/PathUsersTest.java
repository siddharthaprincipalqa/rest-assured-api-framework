package org.example.tests;

import org.example.clients.UsersClient;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class PathUsersTest {

    @Test(dataProvider = "getUserDetails", groups = "regression")
    public void getUsersById(int id, String name, String email, String gender, String status){
        UsersClient usersClient = new UsersClient();
        usersClient.getPublicUser(id)
                .then().log().ifValidationFails()
                    .statusCode(200)
                    .body("id", equalTo(id))
                    .body("name", equalTo(name))
                    .body("email", equalTo(email))
                    .body("gender", equalTo(gender))
                    .body("status", equalTo(status));
    }

    @DataProvider(name = "getUserDetails")
    public Object[][] getUserDetails(){
        return new Object[][]{
                {8698907, "Vivaan Rao", "vivaan_rao_11147@desai.example", "male", "active"}
        };
    }
}
