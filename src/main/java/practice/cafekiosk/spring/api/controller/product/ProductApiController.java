package practice.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import practice.cafekiosk.spring.api.dto.response.ProductResponse;
import practice.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/products")
@Controller
public class ProductApiController {
    private final ProductService productService;

    @GetMapping("/selling")
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        return ResponseEntity.ok(productService.getSellingProducts());
    }
}
