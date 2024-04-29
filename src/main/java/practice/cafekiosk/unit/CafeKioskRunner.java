package practice.cafekiosk.unit;

import practice.cafekiosk.unit.beverage.Americano;
import practice.cafekiosk.unit.beverage.Latte;

public class CafeKioskRunner {
    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);
        System.out.println(">>> add Americano");
        cafeKiosk.add(new Latte(), 1);
        System.out.println(">>> add Latte");
        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("총 주문가격: " + totalPrice);
    }
}
