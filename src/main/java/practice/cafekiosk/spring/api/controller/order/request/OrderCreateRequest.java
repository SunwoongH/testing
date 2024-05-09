package practice.cafekiosk.spring.api.controller.order.request;

import jakarta.validation.constraints.NotEmpty;
import practice.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty(message = "상품번호 리스트는 필수입니다.")
        List<String> productNumbers
) {
    public OrderCreateServiceRequest toServiceRequest() {
        return new OrderCreateServiceRequest(productNumbers);
    }
}
