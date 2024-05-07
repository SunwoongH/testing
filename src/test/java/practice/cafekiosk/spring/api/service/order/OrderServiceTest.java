package practice.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import practice.cafekiosk.spring.api.dto.request.OrderCreateRequest;
import practice.cafekiosk.spring.api.dto.response.OrderResponse;
import practice.cafekiosk.spring.domain.order.OrderRepository;
import practice.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import practice.cafekiosk.spring.domain.product.Product;
import practice.cafekiosk.spring.domain.product.ProductRepository;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;
import practice.cafekiosk.spring.domain.stock.Stock;
import practice.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
//@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        Product product1 = Product.create("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.create("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.create("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "002"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8500);
        assertThat(response.products())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        Product product1 = Product.create("001",
                ProductType.BOTTLE, ProductSellingStatus.SELLING, "탄산수", 4000);
        Product product2 = Product.create("002",
                ProductType.BAKERY, ProductSellingStatus.HOLD, "크루아상", 4500);
        Product product3 = Product.create("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001", "002", "003"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 19500);
        assertThat(response.products())
                .hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 4500),
                        tuple("003", 7000)
                );
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {
        // given
        Product product1 = Product.create("001",
                ProductType.BOTTLE, ProductSellingStatus.SELLING, "탄산수", 4000);
        Product product2 = Product.create("002",
                ProductType.BAKERY, ProductSellingStatus.HOLD, "크루아상", 4500);
        Product product3 = Product.create("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001", "002", "003"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicate() {
        // given
        Product product1 = Product.create("001",
                ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = Product.create("002",
                ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = Product.create("003",
                ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateRequest request = new OrderCreateRequest(List.of("001", "001"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8000);
        assertThat(response.products())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );
    }
}