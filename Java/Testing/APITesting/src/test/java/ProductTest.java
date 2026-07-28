import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class ProductTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProductTest.class);
    @Test
    public void testFetchProducts() {
        RestAssured
                .given()
                    .spec(readRequest)
                    .queryParam("project_id", 28773)
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200);
    }

    private Response createProduct(String name, double price, String category, boolean inStock) {
        Map<String, Object> payload = Map.of(
                "data", Map.of(
                        "name", name,
                        "price", price,
                        "category", category,
                        "in_stock", inStock
                )
        );
        return RestAssured
                .given()
                    .spec(writeRequest)
                    .body(payload)
                .when()
                    .post("collections/products/records")
                .then()
                    .statusCode(201)
                    .extract()
                    .response();
    }

    @Test
    public void testCreateProduct() {
        log.info("testing data creation");
        Response product1 = createProduct("electronic mouse", 25.99, "Electronics", true);
        Response product2 = createProduct("notepad", 7.99, "Stationary", true);

        Assert.assertEquals(product1.getStatusCode(), 201, "Product created successfully");
        Assert.assertEquals(product2.getStatusCode(), 201, "Product created successfully");

        log.info("created record for product1: {}", product1.asString());
        log.info("created record for product2: {}", product2.asString());
    }
}