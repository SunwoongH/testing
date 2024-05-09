package practice.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.cafekiosk.spring.api.common.ApiResponse;
import practice.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import practice.cafekiosk.spring.api.service.product.response.ProductResponse;
import practice.cafekiosk.spring.api.service.product.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/products")
@RestController
public class ProductApiController {
    private final ProductService productService;

    @PostMapping("/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody final ProductCreateRequest request) {
        final ProductResponse response = productService.createProduct(request.toServiceRequest());
        return ApiResponse.ok(response);
    }

    @GetMapping("/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }
}
