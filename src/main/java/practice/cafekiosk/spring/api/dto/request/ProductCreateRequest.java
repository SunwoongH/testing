package practice.cafekiosk.spring.api.dto.request;

import practice.cafekiosk.spring.domain.product.ProductSellingStatus;
import practice.cafekiosk.spring.domain.product.ProductType;

public record ProductCreateRequest(
        ProductType type,
        ProductSellingStatus sellingStatus,
        String name,
        int price
) {
}
