package com.mudosa.musinsa.product.domain.model;

import com.mudosa.musinsa.common.domain.model.BaseEntity;
import com.mudosa.musinsa.product.domain.vo.StockQuantity;
import jakarta.persistence.*;
<<<<<<< HEAD
<<<<<<< HEAD
import lombok.AccessLevel;
import lombok.Builder;
=======

import lombok.AccessLevel;
>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
=======
import lombok.AccessLevel;
import lombok.Builder;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
<<<<<<< HEAD
<<<<<<< HEAD
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventory")
public class Inventory extends BaseEntity {
    
=======
@Table(name = "inventory", 
       indexes = {
           @Index(name = "idx_inventory_prodopt_avail", columnList = "product_option_id, is_available")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uniq_inventory_prodopt", columnNames = {"product_option_id"})
       })
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inventory")
public class Inventory extends BaseEntity {
<<<<<<< HEAD

>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
=======
    
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_inventory_prodopt"))
    private ProductOption productOption;
    
    @Column(name = "stock_quantity", nullable = false)
<<<<<<< HEAD
    private StockQuantity stockQuantity;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    
    @Builder
    public Inventory(ProductOption productOption, StockQuantity stockQuantity, Boolean isAvailable) {
        this.productOption = productOption;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable != null ? isAvailable : true;
    }
    
    // 도메인 로직
    public void modify(StockQuantity stockQuantity, Boolean isAvailable) {
        if (stockQuantity != null) {
            this.stockQuantity = stockQuantity;
        }
        if (isAvailable != null) {
            this.isAvailable = isAvailable;
        } else {
            // isAvailable이 null이면 재고 수량에 따라 자동 설정
            this.isAvailable = this.stockQuantity.getValue() > 0;
        }
    }
    
    public boolean isInStock() {
        return this.stockQuantity.getValue() > 0;
=======

    @Column(name = "product_option_id", nullable = false, unique = true)  // FK (ProductOption)
    private Long productOptionId;

    @Embedded
=======
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    private StockQuantity stockQuantity;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    
    @Builder
    public Inventory(ProductOption productOption, StockQuantity stockQuantity, Boolean isAvailable) {
        this.productOption = productOption;
        this.stockQuantity = stockQuantity;
<<<<<<< HEAD
        this.isAvailable = isAvailable;
>>>>>>> de5afbd (FDBD-43 ✨ feat[product]: 상품 재고 model + vo생성 및 상품 옵션에 재고 관계 추가.)
=======
        this.isAvailable = isAvailable != null ? isAvailable : true;
    }
    
    // 도메인 로직
    public void modify(StockQuantity stockQuantity, Boolean isAvailable) {
        if (stockQuantity != null) {
            this.stockQuantity = stockQuantity;
        }
        if (isAvailable != null) {
            this.isAvailable = isAvailable;
        } else {
            // isAvailable이 null이면 재고 수량에 따라 자동 설정
            this.isAvailable = this.stockQuantity.getValue() > 0;
        }
    }
    
    public boolean isInStock() {
        return this.stockQuantity.getValue() > 0;
>>>>>>> b84d8f5 (FDBD-43 🐛 fix[cart]: 임시 엔티티)
    }
}