package practice.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import practice.cafekiosk.spring.api.dto.request.OrderCreateRequest;
import practice.cafekiosk.spring.api.service.order.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Controller
public class OrderController {
    private OrderService orderService;

    @PostMapping("/new")
    public ResponseEntity<Void> createOrder(@RequestBody final OrderCreateRequest request) {
        orderService.createOrder(request, LocalDateTime.now());
        return ResponseEntity.ok()
                .build();
    }
}
