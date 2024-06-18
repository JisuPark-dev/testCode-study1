package sample.testcode.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {
    
    @DisplayName("재고 수량이 재공된 수량보다 작은지 테스트 한다.")
    @Test
    void isQuantityLessThan(){
        //given
        Stock stock = Stock.create("001", 1);

        //when
        boolean result = stock.isQuantityLessThan(2);

        //then
        assertThat(result).isTrue();
        
    }

    @DisplayName("주어진 갯수만큼 재고를 차감할 수 있다.")
    @Test
    void deductQuantity(){
        //given
        Stock stock = Stock.create("001", 1);
        int quantity = 1;

        //when
        stock.deductQuantity(quantity);

        //then
        assertThat(stock.getQuantity()).isEqualTo(0);

    }
    @DisplayName("재고보다 많은 갯수를 차감하면 예외가 발생한다..")
    @Test
    void deductQuantity2(){
        //given
        Stock stock = Stock.create("001", 1);
        int quantity = 2;

        //when

        //then
        assertThatThrownBy(()-> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다");

    }

}