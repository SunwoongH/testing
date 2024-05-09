package practice.cafekiosk.spring.api.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.cafekiosk.spring.api.common.ApiResponse;
import practice.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import practice.cafekiosk.spring.api.service.order.response.OrderResponse;
import practice.cafekiosk.spring.api.service.order.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;

    @PostMapping("/new")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody final OrderCreateRequest request) {
        final OrderResponse response = orderService.createOrder(request.toServiceRequest(), LocalDateTime.now());
        return ApiResponse.ok(response);
    }
}
