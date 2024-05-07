package practice.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {
    @DisplayName("HANDMADE 상품 타입이 재고 관련 타입인지를 확인한다.")
    @Test
    void containsStockTypeWithHandmade() {
        // given
        ProductType type = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("BOTTLE 상품 타입이 재고 관련 타입인지를 확인한다.")
    @Test
    void containsStockTypeWithBottle() {
        // given
        ProductType type = ProductType.BOTTLE;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("BAKERY 상품 타입이 재고 관련 타입인지를 확인한다.")
    @Test
    void containsStockTypeWithBakery() {
        // given
        ProductType type = ProductType.BAKERY;

        // when
        boolean result = ProductType.containsStockType(type);

        // then
        assertThat(result).isTrue();
    }
}