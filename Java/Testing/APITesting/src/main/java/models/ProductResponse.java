package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    private List<ProductImpl> data;

    public List<ProductImpl> getData() {
        return data;
    }

    public void setData(List<ProductImpl> data) {
        this.data = data;
    }
}
