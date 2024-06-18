package sample.testcode.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.testcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.testcode.spring.api.service.order.response.OrderResponse;
import sample.testcode.spring.domain.order.OrderRepository;
import sample.testcode.spring.domain.orderProduct.OrderProductRepository;
import sample.testcode.spring.domain.product.Product;
import sample.testcode.spring.domain.product.ProductRepository;
import sample.testcode.spring.domain.product.ProductSellingStatus;
import sample.testcode.spring.domain.product.ProductType;
import sample.testcode.spring.domain.stock.Stock;
import sample.testcode.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.testcode.spring.domain.product.ProductSellingStatus.*;
import static sample.testcode.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        // Data cleansing 작업 필요
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 8000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        //when
        OrderResponse orderResponse = orderService.createOrder(request, now);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(now, 8500);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers(){
        //given
        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 8000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        //when
        OrderResponse orderResponse = orderService.createOrder(request, now);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(now, 8000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );

    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성할 수 있다.")
    @Test
    void createOrderWithStock(){
        //given
        Product product1 = createProduct("001", BOTTLE, SELLING, "병음료", 1000);
        Product product2 = createProduct("002", BAKERY, HOLD, "빵", 3000);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "헨드커피", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        //when
        OrderResponse orderResponse = orderService.createOrder(request, now);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime","totalPrice")
                .contains(now, 10000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000));

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1));
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성할 수 있다. 단 갯수가 적을시 예외가 발생한다.")
    @Test
    void createOrderWithNoStock(){
        //given
        Product product1 = createProduct("001", BOTTLE, SELLING, "병음료", 1000);
        Product product2 = createProduct("002", BAKERY, HOLD, "빵", 3000);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "헨드커피", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 0);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        //when
        assertThatThrownBy(() -> orderService.createOrder(request, now))
                .isInstanceOf(IllegalArgumentException.class);
    }


    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}