package com.mudosa.musinsa.product.domain.model;

import com.mudosa.musinsa.common.domain.model.BaseEntity;
<<<<<<< HEAD
import com.mudosa.musinsa.common.vo.Money;
=======
import com.mudosa.musinsa.product.domain.vo.ProductPrice;
<<<<<<< HEAD
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
=======
import com.mudosa.musinsa.product.domain.vo.StockQuantity;
>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_option")
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long productOptionId;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)  
    private Product product;

    // 연관관계 - Inventory 연결
    @OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories = new ArrayList<>();

    // 생성 메서드
    public static ProductOption create(Long productId, ProductPrice productPrice) {
        return new ProductOption(productId, productPrice);
    }

    // 비즈니스 메서드
    public void updatePrice(ProductPrice productPrice) {
        this.productPrice = productPrice;
    }

    public void updateProduct(Long productId) {
        this.productId = productId;
    }

    // 연관관계 메서드 - ProductValueOptionMapping 연결
    public void addProductValueOptionMapping(ProductValueOptionMapping mapping) {
        productValueOptionMappings.add(mapping);
    }

    public void removeProductValueOptionMapping(ProductValueOptionMapping mapping) {
        productValueOptionMappings.remove(mapping);
    }

    // 연관관계 메서드 - Inventory 연결
    public void addInventory(Inventory inventory) {
        inventories.add(inventory);
    }

    // 연관관계 메서드 - Inventory 연결 해제
    public void removeInventory(Inventory inventory) {
        inventories.remove(inventory);
    }

    // 비즈니스 메서드 - 재고 관리
    public Inventory getInventory() {
        return inventories.isEmpty() ? null : inventories.get(0);
    }

    public boolean hasInventory() {
        return !inventories.isEmpty();
    }

    public StockQuantity getTotalStock() {
        return inventories.stream()
            .map(Inventory::getStockQuantity)
            .reduce(StockQuantity.of(0), StockQuantity::add);
    }

    // JPA를 위한 protected 생성자
    protected ProductOption(Long productId, ProductPrice productPrice) {
        this.productId = productId;
        this.productPrice = productPrice;
    }
>>>>>>> 3a8c688 (FDBD-43 ✨ feat[product]: 상품, 상품 옵션(값, 이름, 매핑) model + vo 생성.)
}