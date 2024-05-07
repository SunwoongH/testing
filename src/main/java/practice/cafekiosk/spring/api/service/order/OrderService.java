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
import practice.cafekiosk.spring.domain.product.ProductType;
import practice.cafekiosk.spring.domain.stock.Stock;
import practice.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.productNumbers();
        List<Product> findProducts = findProductsBy(productNumbers);
        List<String> stockProductNumbers = findProducts.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .toList();
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stock = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
        Map<String, Long> productCounting = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        Set<String> distinctStockProductNumbers = new HashSet<>(stockProductNumbers);
        distinctStockProductNumbers.forEach(productNumber -> {
            Stock findStock = stock.get(productNumber);
            int quantity = productCounting.get(productNumber).intValue();
            if (findStock.isQuantityLessThan(quantity)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            findStock.deductQuantity(quantity);
        });
        Order order = Order.create(findProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> findProducts = productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> product = findProducts.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));
        return productNumbers.stream()
                .map(product::get)
                .toList();
    }
}
