package practice.cafekiosk.unit.beverage;

public class Americano implements Beverage {
    @Override
    public String getName() {
        return "americano";
    }

    @Override
    public int getPrice() {
        return 4000;
    }
}
