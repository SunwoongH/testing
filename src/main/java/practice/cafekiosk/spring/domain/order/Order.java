package practice.cafekiosk.spring.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.cafekiosk.spring.domain.BaseEntity;
import practice.cafekiosk.spring.domain.orderproduct.OrderProduct;
import practice.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        products.forEach(product -> OrderProduct.create(this, product));
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return Order.builder()
                .products(products)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProducts.remove(orderProduct);
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }
}
