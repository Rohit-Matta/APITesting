package models.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPOJO {
    private String id;

    @JsonProperty("data")
    private ProductDataPOJO productDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductDataPOJO getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDataPOJO productDetails) {
        this.productDetails = productDetails;
    }
}
