package Products;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.product.ProductPOJO;
import models.product.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.KeyType;
import utils.UseKey;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProductTest.class);
    @Test
    public void testFetchProducts() {
        given()
                    .spec(requestSpec)
                    .queryParam("project_id", 28773)
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200);
    }

    @Test
    private Response createProduct(String name, double price, String category, boolean inStock) {
        Map<String, Object> payload = Map.of(
                "data", Map.of(
                        "name", name,
                        "price", price,
                        "category", category,
                        "in_stock", inStock
                )
        );
        return given()
                    .spec(requestSpec)
                    .body(payload)
                .when()
                    .post("collections/products/records")
                .then()
                    .statusCode(201)
                    .extract()
                    .response();
    }
    /*
    Objective: Verify that a standard GET request with a valid public API key returns a 200 OK status and a paginated list of product records.
    Assertions: Check statusCode(200), content-type is application/json, and verify response body contains key fields (id, title, price, created, updated).
     */
    @Test
    @UseKey(KeyType.PUBLIC)
    public void validateProductList() {
        log.info("validating the id, title, price, created and updated attributes data");
        Response response =
                given()
                    .spec(requestSpec)
                    .queryParam("project_id", 28773)
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("data", not(empty()))
                    .body("data[0].data.name", notNullValue())
                    .body("data[0].data.price", notNullValue())
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract()
                    .response();

        int statusCode = response.getStatusCode();
        String contentType = response.getContentType();

        ProductResponse deserializedResponse = response.as(ProductResponse.class);

        Assert.assertEquals(statusCode, 200, "status code for getting products");
        Assert.assertTrue(contentType.contains("application/json"),  "content type for getting products");
        Assert.assertFalse(deserializedResponse.getData().isEmpty(),"The response returned should not be empty.");
        Assert.assertFalse(deserializedResponse.getData().get(0).getProductDetails().getName().isEmpty(),"There should not be an empty name.");
        Assert.assertTrue(deserializedResponse.getData().get(0).getProductDetails().getPrice() != 0,"The category should not be empty.");


        log.info("The status code is {}", statusCode);
        log.info("The content type is {}", contentType);

        for (ProductPOJO product : deserializedResponse.getData()) {
            log.info("\nresponse data:" +
                    """ 
                    \n| Id: {},
                    | Name: {},
                    | Price: {},
                    | Category: {},
                    | InStock: {}
                    """,
            product.getId(),
            product.getProductDetails().getName(),
            product.getProductDetails().getPrice(),
            product.getProductDetails().getCategory(),
            product.getProductDetails().isIn_stock()
            );
        }
        log.info("Validated the response data, status code and content type.");
    }
    /*
    Objective: Test filtering or pagination parameters (e.g., ?page=1&limit=10 or filtering by specific category/title).
    Assertions: Verify the returned item count matches the specified limit and returned objects meet the filter criteria.
     */
    @Test
    @UseKey(KeyType.PUBLIC)
    public void validatePagination() {
        int page = 1;
        int limit = 10;

        log.info("Provided the number of pages as {} and the limit per page as {}", page, limit);
        Response response =
                given()
                    .spec(requestSpec)
                    //.queryParam("project_id", 28773)
                    .queryParam("page", page)
                    .queryParam("limit", limit)
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200)
                    .body("meta.page", equalTo(page))
                    .body("meta.limit", equalTo(limit))
                        .body("data.size()", lessThanOrEqualTo(limit))
                    .extract()
                    .response();

        int pageNumber = response.jsonPath().get("meta.page");
        int pageSize = response.jsonPath().get("meta.limit");
        int limitSize = response.jsonPath().get("data.size()");

        Assert.assertSame(pageNumber, page, "number of pages is same");
        Assert.assertSame(pageSize, limit, "number of elements per page is same");
        Assert.assertTrue(limitSize <= limit, "data size is less than or equal to the limit");

        log.info("The status code is {}.", response.getStatusCode());
        log.info("The page number is {}.", pageNumber);
        log.info("The page size is {}.", pageSize);
        log.info("The data size {} is less than or equal to max limit {}.", limitSize, limit);
    }

    @Test
    public void validateFiltering() {
        String filterVal = "Food & Drink";
        RestAssured
                .given()
                    .spec(requestSpec)
                    .queryParam("filter", "(category=",filterVal+")")
                .when()
                    .get("/collections/products/records")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                .body("data", hasSize(greaterThan(0)))
                .body("data[5].data.category",equalTo(filterVal))
                .extract()
                .response();

        Assert.assertSame("data[5].data.category", filterVal, "filter value does not exist in the database");
        log.info("Category value is {}.", filterVal);
    }

    @Test
    @UseKey(KeyType.PRIVATE)
    public void testCreateProduct() {
        log.info("testing data creation");
        Response product1 = createProduct("monitor", 299.00, "Electronics", true);

        Assert.assertEquals(product1.getStatusCode(), 201, "Product created successfully");

        log.info("created record for product1: {}", product1.asString());
    }
}