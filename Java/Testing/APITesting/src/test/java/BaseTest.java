import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected RequestSpecification readRequest, writeRequest;
    protected ResponseSpecification readResponse, writeResponse;

    @BeforeClass
    public void setupBaseConfigREAD() {
        readRequest = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .addHeader("x-api-key", "pub_6389ba64ac54c5efb2ca715a357658f1b06a2dbd810d4ae3c5e27082079e107e")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        readResponse = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @BeforeClass
    public void setupBaseConfigWRITE() {
        writeRequest = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .setBasePath("/api")
                .addHeader("x-api-key", "pro_81594188e85702b2d046acadf678db7a2224024768b41a532b440a60d15b7261")
                .addHeader("X-ReqRes-Env", "prod")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        writeResponse = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
