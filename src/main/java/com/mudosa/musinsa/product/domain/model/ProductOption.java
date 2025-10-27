package com.mudosa.musinsa.product.domain.model;

import com.mudosa.musinsa.common.domain.model.BaseEntity;
<<<<<<< HEAD
<<<<<<< HEAD
import com.mudosa.musinsa.common.vo.Money;
=======
import com.mudosa.musinsa.product.domain.vo.ProductPrice;
<<<<<<< HEAD
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
import com.mudosa.musinsa.product.domain.vo.StockQuantity;
>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
=======
import com.mudosa.musinsa.common.vo.Money;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

@Entity
=======
@Entity
@Table(name = "product_option", indexes = {
    @Index(name = "idx_prodopt_product_id", columnList = "product_id"),
    @Index(name = "idx_prodopt_price", columnList = "product_price")
})
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
import java.util.ArrayList;
import java.util.List;

@Entity
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_option")
public class ProductOption extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long productOptionId;
<<<<<<< HEAD
<<<<<<< HEAD
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_option_product"))
    private Product product;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "product_price", nullable = false, precision = 10, scale = 2))
    private Money productPrice;
    
    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductValueOptionMapping> productValueOptionMappings = new ArrayList<>();
    
    @Builder
    public ProductOption(Product product, Money productPrice) {
        this.product = product;
        this.productPrice = productPrice;
    }
    
    // 도메인 로직: 통합 수정
    public void modify(Money productPrice) {
        if (productPrice != null) {
            this.productPrice = productPrice;
        }
    }
=======

    @Column(name = "product_id", nullable = false)  // FK (Product 도메인)
    private Long productId;

    @Embedded
    private ProductPrice productPrice;

    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductValueOptionMapping> productValueOptionMappings = new ArrayList<>();

=======
    
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_option_product"))
    private Product product;
    
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "product_price", nullable = false, precision = 10, scale = 2))
    private Money productPrice;
    
    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductValueOptionMapping> productValueOptionMappings = new ArrayList<>();
    
    @Builder
    public ProductOption(Product product, Money productPrice) {
        this.product = product;
        this.productPrice = productPrice;
    }
    
    // 도메인 로직: 통합 수정
    public void modify(Money productPrice) {
        if (productPrice != null) {
            this.productPrice = productPrice;
        }
    }
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
}