package org.example.tests;


import org.example.data.UserDataFactory;
import org.example.helpers.UserDeleteHelper;
import org.example.clients.UsersClient;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Map;

import static org.example.config.ApiConfig.token;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class DuplicateEmailTest {

    @Test(dataProvider = "duplicateRecords", groups = "regression")
    public void duplicateEmailChecks(String name, String gender, String status){
        String token = token();
        String uniqueEmail = UserDataFactory.uniqueEmail();
        Map<String, Object> requestBody = UserDataFactory.userBody(name, uniqueEmail, gender, status);
        UsersClient usersClient = new UsersClient();
        int userId = usersClient.createUser(token, requestBody)
                .then().log().ifValidationFails()
                .statusCode(201)
                .body("id", greaterThan(0))
                .extract().path("id");
        UserDeleteHelper deleteUser = new UserDeleteHelper();
        try{
            usersClient.createUser(token, requestBody)
                    .then().log().ifValidationFails()
                    .statusCode(422)
                    .body("size()", equalTo(1))
                    .body("[0].field", equalTo("email"))
                    .body("[0].message", equalTo("has already been taken"));
        }finally {
            deleteUser.deleteUser(userId, token);
            deleteUser.verifyUserIsDeleted(userId, token);
        }
    }

    @Test(dataProvider = "invalidEmail", groups = "regression")
    public void invalidEmailChecks(String email){
        String token = token();
        Map<String, Object> requestBody = UserDataFactory.userBody("Email Validation Practice", email, "female", "inactive");
        UsersClient usersClient = new UsersClient();
        usersClient.createUser(token, requestBody)
                .then().log().ifValidationFails()
                .statusCode(422)
                .body("size()", equalTo(1))
                .body("[0].field", equalTo("email"))
                .body("[0].message", equalTo("is invalid"));

    }

    @Test(groups = "regression")
    public void multipleValidationErrorsAreReturned(){
        String token = token();
        Map<String, Object> requestBody = UserDataFactory.userBody("", "Not an email", "female", "inactive");
        UsersClient usersClient = new UsersClient();
        usersClient.createUser(token, requestBody)
                .then().log().ifValidationFails()
                .statusCode(422)
                .body("size()", equalTo(2))
                .body("find { it.field == 'name' }.message", equalTo("can't be blank"))
                .body("find { it.field == 'email' }.message", equalTo("is invalid"));
    }

    @DataProvider(name="duplicateRecords")
    public Object[][] duplicateRecords(){
        return new Object[][]{
                {"Sidd", "male", "active"},
                {"Sid", "male", "active"}
        };
    }

    @DataProvider(name="invalidEmail")
    public Object[][] invalidEmail(){
        return new Object[][]{
                {"not-an-email"},
                {"qa.example.com"},
                {"qa@"}
        };
    }
}
