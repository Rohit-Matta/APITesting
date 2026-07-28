package models.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private List<ProductPOJO> data;

    public List<ProductPOJO> getData() {
        return data;
    }

    public void setData(List<ProductPOJO> data) {
        this.data = data;
    }
}
