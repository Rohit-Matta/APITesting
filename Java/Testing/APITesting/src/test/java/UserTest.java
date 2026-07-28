import io.restassured.RestAssured;
import models.Users.UserDataPOJO;
import models.Users.UsersPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class UserTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(UserTest.class);
    @Test
    public void testFetchUsers() {
        logger.info("testing testFetchUsers test case");
        UsersPOJO response = RestAssured
                .given()
                    .spec(readRequest)
                    .queryParam("page", 2)
                .when()
                    .get("/users")
                .then()
                    .spec(readResponse)
                    .statusCode(200)
                    .body("page", equalTo(2))
                    .body("data", hasSize(6))
                    .body("data[0].first_name", equalTo("Michael"))
                    .body("data.email", hasItem("lindsay.ferguson@reqres.in"))
                    .extract()
                    .as(UsersPOJO.class);
        UserDataPOJO firstUser = response.getUsers().get(0);
        Assert.assertEquals(response.getPage(), 2);
        Assert.assertEquals(response.getUsers().size(), 6);
        Assert.assertEquals(firstUser.getFirstName(), "Michael");
        Assert.assertEquals(firstUser.getEmail(), "michael.lawson@reqres.in");

        logger.info("""
                        
                        Successfully validated payload:
                        | Page: {}
                        | Total Users: {}
                        | First name: {}
                        | Email: {}""",
                response.getPage(), response.getUsers().size(), firstUser.getFirstName(), firstUser.getEmail());
    }
}
