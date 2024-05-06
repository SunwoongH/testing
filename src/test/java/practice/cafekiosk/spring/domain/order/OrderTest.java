package practice.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import practice.cafekiosk.spring.domain.product.Product;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        Product product1 = Product.createProduct("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.createProduct("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.createProduct("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        List<Product> products = List.of(product1, product2, product3);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(15500);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다")
    @Test
    void init() {
        // given
        Product product1 = Product.createProduct("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.createProduct("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.createProduct("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        List<Product> products = List.of(product1, product2, product3);

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        Product product1 = Product.createProduct("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.createProduct("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.createProduct("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        List<Product> products = List.of(product1, product2, product3);
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }
}