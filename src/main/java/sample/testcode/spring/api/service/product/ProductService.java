package sample.testcode.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.testcode.spring.api.controller.product.dto.ProductCreateRequest;
import sample.testcode.spring.api.service.product.response.ProductResponse;
import sample.testcode.spring.domain.product.Product;
import sample.testcode.spring.domain.product.ProductRepository;
import sample.testcode.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
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
