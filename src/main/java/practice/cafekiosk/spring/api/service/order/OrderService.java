package practice.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.cafekiosk.spring.api.dto.request.OrderCreateRequest;
import practice.cafekiosk.spring.api.dto.response.OrderResponse;
import practice.cafekiosk.spring.domain.order.Order;
import practice.cafekiosk.spring.domain.order.OrderRepository;
import practice.cafekiosk.spring.domain.product.Product;
import practice.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.productNumbers();
        List<Product> findProducts = productRepository.findAllByProductNumberIn(productNumbers);
        Order order = Order.create(findProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }
}
