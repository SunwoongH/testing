package practice.cafekiosk.spring.api.service.order.response;

import lombok.AccessLevel;
import lombok.Builder;
import practice.cafekiosk.spring.api.service.product.response.ProductResponse;
import practice.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record OrderResponse(
        Long id,
        int totalPrice,
        LocalDateTime registeredDateTime,
        List<ProductResponse> products
) {
    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(order.getOrderProducts()
                        .stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .toList())
                .build();
    }
}
