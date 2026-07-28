import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProductTest.class);
    @Test
    public void testFetchProducts() {
        RestAssured
                .given()
                    .spec(requestSpec)
                    .queryParam("project_id", 28773)
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200);
    }
}
