package practice.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import practice.cafekiosk.unit.beverage.Americano;
import practice.cafekiosk.unit.beverage.Beverage;
import practice.cafekiosk.unit.beverage.Latte;
import practice.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {
    @Test
    void add() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        cafeKiosk.add(new Americano(), 1);

        // then
        List<Beverage> beverages = cafeKiosk.getBeverages();
        assertThat(beverages).hasSize(1);
        Beverage findAmericano = beverages.get(0);
        assertThat(findAmericano.getName()).isEqualTo("americano");
    }

    @Test
    void addSeveralBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when
        cafeKiosk.add(americano, 2);

        // then
        List<Beverage> beverages = cafeKiosk.getBeverages();
        assertThat(beverages.get(0)).isEqualTo(americano);
        assertThat(beverages.get(1)).isEqualTo(americano);
    }

    @Test
    void addZeroBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        // when & then
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    void remove() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        // when
        cafeKiosk.remove(americano);

        // then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void clear() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);

        // when
        cafeKiosk.clear();

        // then
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    void createOrderWithBasic() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        // when
        Order order = cafeKiosk.createOrder();

        // then
        List<Beverage> beverages = order.getBeverages();
        assertThat(beverages).hasSize(1);
        Beverage findAmericano = beverages.get(0);
        assertThat(findAmericano.getName()).isEqualTo("americano");
    }

    @Test
    void createOrderWithRefactoring() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        // when
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024, 4, 30, 10, 0));

        // then
        List<Beverage> beverages = order.getBeverages();
        assertThat(beverages).hasSize(1);
        Beverage findAmericano = beverages.get(0);
        assertThat(findAmericano.getName()).isEqualTo("americano");
    }

    @Test
    void createOrderOutsideOpenTime() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        // when & then
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2024, 4, 30, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}