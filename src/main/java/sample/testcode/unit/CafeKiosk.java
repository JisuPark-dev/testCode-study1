package sample.testcode.unit;

import lombok.Getter;
import sample.testcode.unit.beverage.Beverage;
import sample.testcode.unit.order.Order;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    public static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
    public static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(22, 0);

    private final List<Beverage> beverages = new ArrayList<>();
    private static int totalPrice = 0;


    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count <= 0) {
            throw new InvalidParameterException("갯수가 0개 이하일 수 없습니다");
        }
        for (int i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage)  {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        for (Beverage beverage : beverages) {
            totalPrice += beverage.getPrice();
        }
        return totalPrice ;
    }

    public Order createOrder() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime currentTime = now.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문시간이 아닙니다. 관리자에게 문의하세요");
        }
        return new Order(LocalDateTime.now(), beverages);
    }
    public Order createOrder(LocalDateTime time) {
        LocalTime currentTime = time.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("주문시간이 아닙니다. 관리자에게 문의하세요");
        }
        return new Order(time, beverages);
    }
}
