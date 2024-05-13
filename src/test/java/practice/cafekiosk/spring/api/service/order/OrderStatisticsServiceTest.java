package practice.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import practice.cafekiosk.spring.client.mail.MailSendClient;
import practice.cafekiosk.spring.domain.history.mail.MailSendHistory;
import practice.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import practice.cafekiosk.spring.domain.order.Order;
import practice.cafekiosk.spring.domain.order.OrderRepository;
import practice.cafekiosk.spring.domain.order.OrderStatus;
import practice.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import practice.cafekiosk.spring.domain.product.Product;
import practice.cafekiosk.spring.domain.product.ProductRepository;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderStatisticsServiceTest {
    @Autowired
    OrderStatisticsService orderStatisticsService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;
    @MockBean
    MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
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
        orderRepository.saveAll(List.of(order1, order2, order3, order4));
        when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2024, 5, 13), "test@test.com");

        // then
        assertThat(result).isTrue();
        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 31000원 입니다.");
    }
}