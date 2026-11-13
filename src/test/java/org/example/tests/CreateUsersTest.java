package org.example.tests;

import io.restassured.response.Response;
import org.example.data.UserDataFactory;
import org.example.helpers.UpdatedUserStatus;
import org.example.helpers.UserApiHelper;
import org.example.helpers.UserDeleteHelper;
import org.example.clients.UsersClient;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;
import static org.example.config.ApiConfig.token;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import org.example.helpers.SchemaValidationHelper;

public class CreateUsersTest {

    @Test(dataProvider = "createUsersData", groups = "regression")
    public void validTokenCreateNewUser(String name, String gender, String status){
        String token = token();
        String uniqueEmail = UserDataFactory.uniqueEmail();
        Map<String, Object> requestBody = UserDataFactory.userBody(name, uniqueEmail, gender, status);
        UsersClient usersClient = new UsersClient();
        Response response = usersClient.createUser(token, requestBody);
        response.then().log().ifValidationFails()
                .statusCode(201)
                .body("id", greaterThan(0));
        int userId = response.jsonPath().getInt("id");
        UserApiHelper getDetails = new UserApiHelper();
        UpdatedUserStatus uUStatus = new UpdatedUserStatus();
        UserDeleteHelper deleteUser = new UserDeleteHelper();
        try{
            SchemaValidationHelper.validate(
                    response,
                    "schemas/user-schema.json"
            );
            response.then().log().ifValidationFails()
                    .body("name", equalTo(name))
                    .body("email", equalTo(uniqueEmail))
                    .body("gender", equalTo(gender))
                    .body("status", equalTo(status));
            getDetails.getUsersById(userId, token, name, uniqueEmail, gender, status);
            String updatedStatus =
                    status.equals("active") ? "inactive" : "active";
            uUStatus.updateStatus(userId, token, updatedStatus);
            getDetails.getUsersById(userId, token, name, uniqueEmail, gender, updatedStatus);
        }finally {
            deleteUser.deleteUser(userId, token);
            deleteUser.verifyUserIsDeleted(userId, token);
        }
    }

    @DataProvider(name = "createUsersData")
    public Object[][] createUsersData(){
        return new Object[][]{
                {"Jane Ko6", "male", "active"},
                {"Sane Jo6", "male", "inactive"}
        };
    }
}
