package tests;

import models.ProductImpl;
import models.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ProductTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductTest.class);
    @Test
    public void testFindProductByName() throws IOException {
        logger.info("Start test execution: testFindProductByName");
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/products.json");

        logger.debug("Attempting to parse json file: {}", jsonFile.getAbsolutePath());
        ProductResponse response = mapper.readValue(jsonFile, ProductResponse.class);
        logger.info("Successfully deserialized {} items into ProductResponse model.", response.getData().size());

        String targetName = "Wireless Headphones";
        logger.debug("Filtering dataset dynamically for product name: {}", targetName);

        ProductImpl product = response.getData().stream()
                .filter(data -> data.getProductDetails() != null && targetName.equals(data.getProductDetails().getName()))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(product, "Target product '" + targetName + "' not found in dataset!");

        logger.info("Product match found. Target ID: {} | In Stock {}", product.getId(), product.isIn_stock());
    }
}