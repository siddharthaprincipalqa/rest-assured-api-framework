package org.example.tests;

import io.restassured.response.Response;
import org.example.clients.UsersClient;
import org.example.data.UserDataFactory;
import org.example.helpers.UserDeleteHelper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;

import static org.example.config.ApiConfig.token;
import static org.hamcrest.Matchers.equalTo;

public class UserValidationTest {

    @Test(dataProvider = "blankNames", groups = "regression")
    public void missingNameIsRejected(String name){
        String token = token();
        String uniqueEmail = UserDataFactory.uniqueEmail();
        Map<String, Object> requestBody = UserDataFactory.userBody(name, uniqueEmail, "male", "active");
        UsersClient usersClient = new UsersClient();
        Response response =
                usersClient.createUser(token, requestBody);
        response.then()
                .log().ifValidationFails().statusCode(422)
                .body("size()", equalTo(1))
                .body("[0].field", equalTo("name"))
                .body("[0].message", equalTo("can't be blank"));
    }
    @Test(dataProvider = "nameLengthCases", groups = "regression")
    public void nameLengthIsValidated(int length, int expectedStatus) {
        String token = token();
        Map<String, Object> requestBody =
                UserDataFactory.userBody(
                        UserDataFactory.nameOfLength(length),
                        UserDataFactory.uniqueEmail(),
                        "male",
                        "active"
                );
        UsersClient usersClient = new UsersClient();
        Response response = usersClient.createUser(token, requestBody);
        Integer createdUserId = null;
        if (response.statusCode() == 201) {
            createdUserId = response.jsonPath().getInt("id");
        }
        try {
            response.then()
                    .log().ifValidationFails()
                    .statusCode(expectedStatus);
        } finally {
            if (createdUserId != null) {
                UserDeleteHelper deleteHelper = new UserDeleteHelper();
                deleteHelper.deleteUser(createdUserId, token);
                deleteHelper.verifyUserIsDeleted(createdUserId, token);
            }
        }
    }

    @DataProvider(name="blankNames")
    public Object[][] blankNames(){
        return new Object[][]{
                {""},
                {"   "},
                {null}
        };
    }
    @DataProvider(name = "nameLengthCases")
    public Object[][] nameLengthCases() {
        return new Object[][] {
                {200, 201},
                {201, 422}
        };
    }
}
