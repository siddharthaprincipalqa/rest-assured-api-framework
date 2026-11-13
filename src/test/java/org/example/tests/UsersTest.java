package org.example.tests;

import org.example.clients.UsersClient;
import org.testng.annotations.Test;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class UsersTest {

    @Test(groups = {"smoke", "regression"})
    public void getUserReturns200() {
        UsersClient usersClient = new UsersClient();
        usersClient.getUsers(Map.of())
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("status", hasItem("active"));
    }

    @Test(groups = "regression")
    public void getActiveUsers() {
        UsersClient usersClient = new UsersClient();
        usersClient.getUsers(Map.of("status", "active"))
                .then().log().ifValidationFails()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("status", everyItem(equalTo("active")));
    }

    @Test(groups = "regression")
    public void getActiveGenderUsers() {
        UsersClient usersClient = new UsersClient();
        usersClient.getUsers(Map.of(
                "status", "active",
                "gender", "male",
                "page", 2,
                "per_page", 5))
                .then().log().ifValidationFails()
                .statusCode(200)
                .header("X-Pagination-Page", equalTo("2"))
                .header("X-Pagination-Limit", equalTo("5"))
                .body("size()", lessThanOrEqualTo(5))
                .body("status", everyItem(equalTo("active")))
                .body("gender", everyItem(equalTo("male")));
    }
}
