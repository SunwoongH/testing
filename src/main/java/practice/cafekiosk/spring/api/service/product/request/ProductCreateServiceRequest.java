package practice.cafekiosk.spring.api.service.product.request;

import lombok.Builder;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;

@Builder
public record ProductCreateServiceRequest(
        ProductType type,
        ProductSellingStatus sellingStatus,
        String name,
        int price
) {
}
