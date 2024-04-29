package practice.cafekiosk.unit.beverage;

import practice.cafekiosk.unit.beverage.Beverage;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "latte";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
