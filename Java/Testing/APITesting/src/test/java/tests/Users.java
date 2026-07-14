package tests;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Users {
    private final String API_KEY="reqres_467e374f6fe34379827620c9056d7458";
    @Test
    public void getUser() {
        given()
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json")
        .when()
                .get("https://reqres.in/api/users/2")
        .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    public void getUsers() {
        given()
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json")
        .when()
            .get("https://reqres.in/api/users?page=2")
        .then()
            .statusCode(200)
                .log().all()
                .body("page", equalTo(2));
    }

    @Test
    public void createUser() {
        HashMap<String,Object> data = new HashMap<>();
        data.put("email", "anthony.edwards@mtimberwolves.com");
        data.put("firstName", "Anthony");
        data.put("lastName", "Edwards");

        given()
                .header("x-api-key", API_KEY)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .body(data)
        .when()
                .post("https://reqres.in/api/users")
        .then()
                .statusCode(201)
                .log().all();
    }
}
