package sample.testcode.unit.beverage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AmericanoTest {

    @DisplayName("이름 검증 성공")
    @Test
    void getName(){
        //given
        Americano americano = new Americano();
        //when

        //then
        assertEquals(americano.getName(), "아메리카노");
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @DisplayName("가격 검증 성공")
    @Test
    void getPrice(){
        //given
        Americano americano = new Americano();
        //when
        
        //then
        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}