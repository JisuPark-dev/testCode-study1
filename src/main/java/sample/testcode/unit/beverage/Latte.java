package sample.testcode.unit.beverage;

import sample.testcode.unit.beverage.Beverage;

public class Latte implements Beverage {
    @Override
    public int getPrice() {
        return 4500;
    }

    @Override
    public String getName() {
        return "라떼";
    }
}
