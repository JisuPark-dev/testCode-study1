package sample.testcode.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class ProductTypeTest {

    @DisplayName("상품 타입이 제고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeFalse(){
        //given
        ProductType handmade = ProductType.HANDMADE;

        //when
        boolean result = ProductType.containsStockType(handmade);

        //then
        assertThat(result).isFalse();
        
    }

    @DisplayName("상품 타입이 제고 관련 타입인지를 체크한다.")
    @Test
    void containsStockTypeTrue(){
        //given
        ProductType handmade = ProductType.BAKERY;

        //when
        boolean result = ProductType.containsStockType(handmade);

        //then
        assertThat(result).isTrue();

    }

}