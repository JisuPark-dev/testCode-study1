package sample.testcode.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.testcode.spring.api.controller.product.dto.ProductCreateRequest;
import sample.testcode.spring.api.service.product.response.ProductResponse;
import sample.testcode.spring.domain.product.Product;
import sample.testcode.spring.domain.product.ProductRepository;
import sample.testcode.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();
        return ProductResponse.builder()
                .productNumber(nextProductNumber)
                .name(request.getName())
                .type(request.getType())
                .price(request.getPrice())
                .sellingStatus(request.getSellingStatus())
                .build();
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> productList = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return productList.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());


    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber == null) {
            return "001";
        }else{
            Integer latestProductNumberInt = Integer.valueOf(latestProductNumber);
            int NextProductNumberInt = latestProductNumberInt + 1;
            return String.format("%03d", NextProductNumberInt);
        }
    }
}
