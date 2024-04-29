package practice.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import practice.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class Order {
    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
