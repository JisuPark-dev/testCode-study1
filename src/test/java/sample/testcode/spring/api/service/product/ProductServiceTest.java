package sample.testcode.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.testcode.spring.api.controller.product.dto.ProductCreateRequest;
import sample.testcode.spring.api.service.product.response.ProductResponse;
import sample.testcode.spring.domain.product.Product;
import sample.testcode.spring.domain.product.ProductRepository;
import sample.testcode.spring.domain.product.ProductSellingStatus;
import sample.testcode.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.testcode.spring.domain.product.ProductSellingStatus.*;
import static sample.testcode.spring.domain.product.ProductType.HANDMADE;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() { // tearDown = 구조물의 해체, 혹은 철거를 뜻한다.
        productRepository.deleteAllInBatch();
    }
    
    @DisplayName("상품이 아무것도 없을 때, 상품이 상품번호 001로 잘 만들어진다.")
    @Test
    void createProductNumberWhenProductIsEmpty(){
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("신규상품")
                .price(10000)
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("name","price","sellingStatus","type", "productNumber")
                .containsExactlyInAnyOrder("신규상품", 10000, SELLING, HANDMADE, "001");

        List<Product> all = productRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("name","price","sellingStatus","type", "productNumber")
                .containsExactlyInAnyOrder(
                        tuple("신규상품", 10000, SELLING, HANDMADE, "001")
                );
    }

    @DisplayName("신규상품을 등록할 때, 상품번호가 가장 최근 상품번호 + 1로 만들어진다.")
    @Test
    void createProductWhenProductIsNotNull(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product1);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("신규상품")
                .price(10000)
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("name","price","sellingStatus","type", "productNumber")
                .containsExactlyInAnyOrder("신규상품", 10000, SELLING, HANDMADE, "002");

        List<Product> all = productRepository.findAll();
        assertThat(all).hasSize(2)
                .extracting("name","price","sellingStatus","type", "productNumber")
                .containsExactlyInAnyOrder(
                        tuple("신규상품", 10000, SELLING, HANDMADE, "002"),
                        tuple("아메리카노", 4000, SELLING, HANDMADE, "001")
                );
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }


}