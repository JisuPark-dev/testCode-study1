package sample.testcode.spring.api.controller.product.dto;

import lombok.Builder;
import lombok.Getter;
import sample.testcode.spring.domain.product.ProductSellingStatus;
import sample.testcode.spring.domain.product.ProductType;

@Getter
public class ProductCreateRequest {

    private ProductType type;

    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
