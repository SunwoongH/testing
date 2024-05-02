package practice.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.cafekiosk.spring.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String productNumber;
    @Enumerated(EnumType.STRING)
    private ProductType type;
    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;
}
