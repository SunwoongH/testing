package practice.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.cafekiosk.spring.api.dto.response.ProductResponse;
import practice.cafekiosk.spring.domain.product.ProductRepository;
import practice.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        return productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay())
                .stream()
                .map(ProductResponse::of)
                .toList();
    }
}
