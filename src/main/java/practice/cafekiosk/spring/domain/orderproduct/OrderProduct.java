package practice.cafekiosk.spring.domain.orderproduct;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.cafekiosk.spring.domain.common.BaseEntity;
import practice.cafekiosk.spring.domain.order.Order;
import practice.cafekiosk.spring.domain.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderproduct_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }

    public static OrderProduct create(Order order, Product product) {
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .build();
        orderProduct.changeOrder(order);
        return orderProduct;
    }

    private void changeOrder(Order order) {
        if (this.order != null) {
            this.order.removeOrderProduct(this);
        }
        this.order = order;
        order.addOrderProduct(this);
    }
}
