package practice.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import practice.cafekiosk.spring.domain.product.Product;
import practice.cafekiosk.spring.domain.product.ProductRepository;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @DisplayName("주어진 시작일과 종료일 사이에 생성된 주어진 주문 상태의 주문들을 조회한다.")
    @Test
    void findOrdersBy() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 5, 13, 0, 0);
        Product product1 = Product.create("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.create("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.create("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);
        Order order1 = Order.create(products, OrderStatus.PAYMENT_COMPLETED,
                LocalDateTime.of(2024, 5, 12, 23, 59, 59));
        Order order2 = Order.create(products, OrderStatus.PAYMENT_COMPLETED,
                now);
        Order order3 = Order.create(products, OrderStatus.PAYMENT_COMPLETED,
                LocalDateTime.of(2024, 5, 13, 23, 59, 59));
        Order order4 = Order.create(products, OrderStatus.PAYMENT_COMPLETED,
                LocalDateTime.of(2024, 5, 14, 0, 0, 0));
        List<Order> orders = List.of(order1, order2, order3, order4);
        orderRepository.saveAll(orders);

        // when
        List<Order> findOrders = orderRepository.findOrdersBy(
                LocalDateTime.of(2024, 5, 13, 0, 0),
                LocalDateTime.of(2024, 5, 14, 0, 0),
                OrderStatus.PAYMENT_COMPLETED
        );

        // then
        assertThat(findOrders).hasSize(2)
                .extracting("orderStatus", "totalPrice", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        tuple(OrderStatus.PAYMENT_COMPLETED, 15500, now),
                        tuple(OrderStatus.PAYMENT_COMPLETED, 15500, LocalDateTime.of(2024, 5, 13, 23, 59, 59))
                );
    }
}