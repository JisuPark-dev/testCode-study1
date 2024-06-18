package sample.testcode.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;
import sample.testcode.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static sample.testcode.spring.domain.order.OrderStatus.INIT;
import static sample.testcode.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.testcode.spring.domain.product.ProductType.HANDMADE;

class OrderTest {
    @DisplayName("주문 생성시에 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice(){
        //given
        List<Product> products = List.of(createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.calculateTotalPrice(products)).isEqualTo(3000);

    }

    @DisplayName("주문 생성시에 주문 상태는 INIT이다.")
    @Test
    void initProductStatus(){
        //given
        List<Product> products = List.of(createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(INIT);

    }

    @DisplayName("주문 생성시 등록 시간을 기록한다.")
    @Test
    void registeredDateTime(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(createProduct("001", 1000),
                createProduct("002", 2000)
        );

        //when
        Order order = Order.create(products, registeredDateTime);

        //then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);

    }

    private Product createProduct( String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }

}