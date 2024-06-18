package sample.testcode.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.testcode.unit.beverage.Americano;
import sample.testcode.unit.beverage.Latte;
import sample.testcode.unit.order.Order;

import java.security.InvalidParameterException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {
    
    @DisplayName("a면 b다.")
    @Test
    void test(){
        //given
        
        //when
        
        //then
        
    }

    @DisplayName("음료 한잔 추가 성공")
    @Test
    void add(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        //when
        cafeKiosk.add(new Americano());
        //then
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("음료 여러잔 추가 성공")
    @Test
    void addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("음료 0장 추가 실패")
    @Test
    void failToAddZeroBeverage(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //then
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("갯수가 0개 이하일 수 없습니다");
    }


    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);
        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        //when
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        cafeKiosk.clear();

        //then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("총 주문 금액 계산")
    @Test
    void calculateTotalPrice(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);
        //when
        int totalPrice = cafeKiosk. calculateTotalPrice();
        //then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @DisplayName("주문 성공")
    @Test
    void createOrder(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        //when
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 3, 27, 12, 0));

        //then
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getOrderDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 27, 12, 0));


    }

    @DisplayName("주문 실패")
    @Test
    void failToCreateOrder(){
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        //when
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 3, 27, 23,0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문시간이 아닙니다. 관리자에게 문의하세요");

    }


}